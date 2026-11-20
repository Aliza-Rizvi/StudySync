package com.studysync.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A study group for one course. Table: study_groups
 * (We do not call the table "groups" because GROUPS is a reserved word in MySQL 8.)
 */
@Entity
@Table(name = "study_groups")
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by")
    private Student createdBy;

    private int maxMembers = 6;

    private LocalDateTime createdAt = LocalDateTime.now();

    /** Members of the group (extra table: group_members) */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> members = new HashSet<>();

    public StudyGroup() {
    }

    public StudyGroup(String name, String description, Course course, Student createdBy, int maxMembers) {
        this.name = name;
        this.description = description;
        this.course = course;
        this.createdBy = createdBy;
        this.maxMembers = maxMembers;
    }

    public boolean hasMember(Long studentId) {
        return members.stream().anyMatch(m -> m.getId().equals(studentId));
    }

    public boolean isFull() {
        return members.size() >= maxMembers;
    }

    public boolean isCreatedBy(Long studentId) {
        return createdBy != null && createdBy.getId().equals(studentId);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Student getCreatedBy() { return createdBy; }
    public void setCreatedBy(Student createdBy) { this.createdBy = createdBy; }
    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Student> getMembers() { return members; }
    public void setMembers(Set<Student> members) { this.members = members; }
}