package com.studysync.service;

import com.studysync.config.ConfigurationManager;
import com.studysync.model.*;
import com.studysync.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Create groups, suggest groups, join requests and invitations. */
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final NotificationRepository notificationRepository;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository,
                        CourseRepository courseRepository, JoinRequestRepository joinRequestRepository,
                        NotificationRepository notificationRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.notificationRepository = notificationRepository;
    }

    public StudyGroup createGroup(Long creatorId, String name, Long courseId, String description, int maxMembers) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name is required.");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("Please choose a course.");
        }
        Student creator = student(creatorId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found."));
        if (!creator.isEnrolledIn(courseId)) {
            throw new IllegalArgumentException("You can only create groups for courses you are enrolled in.");
        }
        int limit = ConfigurationManager.getInstance().getMaxGroupSize();
        if (maxMembers < 2 || maxMembers > limit) {
            maxMembers = limit;
        }
        StudyGroup group = new StudyGroup(name.trim(), description, course, creator, maxMembers);
        group.getMembers().add(creator);
        return groupRepository.save(group);
    }

    public StudyGroup getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found."));
    }

    public List<StudyGroup> myGroups(Long studentId) {
        return groupRepository.findByMembers_Id(studentId);
    }

    /** Groups of my courses that I am not in and that still have space */
    public List<StudyGroup> suggestGroups(Long studentId) {
        Student me = student(studentId);
        Map<Long, StudyGroup> suggestions = new LinkedHashMap<>();
        for (Course course : me.getCourses()) {
            for (StudyGroup group : groupRepository.findByCourse_Id(course.getId())) {
                if (!group.hasMember(studentId) && !group.isFull()) {
                    suggestions.put(group.getId(), group);
                }
            }
        }
        return new ArrayList<>(suggestions.values());
    }

    /** A student asks to join. The group creator must accept. */
    public JoinRequest requestToJoin(Long groupId, Long studentId) {
        StudyGroup group = getGroup(groupId);
        Student student = student(studentId);
        checkCanJoin(group, studentId);
        JoinRequest request = joinRequestRepository.save(new JoinRequest(group, student, RequestType.REQUEST));
        notify(group.getCreatedBy(), student.getFullName() + " wants to join your group \"" + group.getName() + "\".",
                "/groups");
        return request;
    }

    /** A group member invites another student. The invited student must accept. */
    public JoinRequest invite(Long groupId, Long inviterId, Long inviteeId) {
        StudyGroup group = getGroup(groupId);
        if (!group.hasMember(inviterId)) {
            throw new IllegalArgumentException("Only group members can invite others.");
        }
        Student invitee = student(inviteeId);
        Student inviter = student(inviterId);
        checkCanJoin(group, inviteeId);
        JoinRequest invitation = joinRequestRepository.save(new JoinRequest(group, invitee, RequestType.INVITE));
        notify(invitee, inviter.getFullName() + " invited you to join \"" + group.getName() + "\".", "/groups");
        return invitation;
    }

    public void acceptRequest(Long requestId, Long actingStudentId) {
        JoinRequest request = pendingRequest(requestId, actingStudentId);
        StudyGroup group = request.getGroup();
        if (group.isFull()) {
            throw new IllegalArgumentException("Sorry, this group is already full.");
        }
        group.getMembers().add(request.getStudent());
        groupRepository.save(group);
        request.setStatus(RequestStatus.ACCEPTED);
        joinRequestRepository.save(request);

        if (request.getRequestType() == RequestType.REQUEST) {
            notify(request.getStudent(), "Your request to join \"" + group.getName() + "\" was accepted.",
                    "/groups/" + group.getId());
        } else {
            notify(group.getCreatedBy(), request.getStudent().getFullName() + " joined \"" + group.getName() + "\".",
                    "/groups/" + group.getId());
        }
    }

    public void rejectRequest(Long requestId, Long actingStudentId) {
        JoinRequest request = pendingRequest(requestId, actingStudentId);
        request.setStatus(RequestStatus.REJECTED);
        joinRequestRepository.save(request);
    }

    /** Join requests waiting for MY answer (I am the group creator) */
    public List<JoinRequest> pendingRequestsForCreator(Long studentId) {
        return joinRequestRepository.findByGroup_CreatedBy_IdAndRequestTypeAndStatus(
                studentId, RequestType.REQUEST, RequestStatus.PENDING);
    }

    /** Invitations waiting for MY answer */
    public List<JoinRequest> pendingInvitesFor(Long studentId) {
        return joinRequestRepository.findByStudent_IdAndRequestTypeAndStatus(
                studentId, RequestType.INVITE, RequestStatus.PENDING);
    }

    public void leaveGroup(Long groupId, Long studentId) {
        StudyGroup group = getGroup(groupId);
        if (group.isCreatedBy(studentId)) {
            throw new IllegalArgumentException("The group creator cannot leave the group.");
        }
        group.getMembers().removeIf(member -> member.getId().equals(studentId));
        groupRepository.save(group);
    }

    // ---------- helpers ----------

    private void checkCanJoin(StudyGroup group, Long studentId) {
        if (group.hasMember(studentId)) {
            throw new IllegalArgumentException("This student is already a member of the group.");
        }
        if (group.isFull()) {
            throw new IllegalArgumentException("Sorry, this group is already full.");
        }
        if (joinRequestRepository.existsByGroup_IdAndStudent_IdAndStatus(group.getId(), studentId, RequestStatus.PENDING)) {
            throw new IllegalArgumentException("A request for this group is already pending.");
        }
    }

    /** Finds a PENDING request and checks that this student is allowed to answer it */
    private JoinRequest pendingRequest(Long requestId, Long actingStudentId) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found."));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("This request was already answered.");
        }
        boolean allowed = request.getRequestType() == RequestType.REQUEST
                ? request.getGroup().isCreatedBy(actingStudentId)
                : request.getStudent().getId().equals(actingStudentId);
        if (!allowed) {
            throw new IllegalArgumentException("You cannot answer this request.");
        }
        return request;
    }

    private Student student(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
    }

    private void notify(Student to, String message, String link) {
        notificationRepository.save(new Notification(to, message, link));
    }
}