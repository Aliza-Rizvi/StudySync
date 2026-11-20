package com.studysync.model;

import jakarta.persistence.*;

/** A university course, e.g. "SE-301 Software Construction and Development". Table: courses */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String department;

    public Course() {
    }

    public Course(String code, String name, String department) {
        this.code = code;
        this.name = name;
        this.department = department;
    }

    /** Used on pages, e.g. "SE-301 - Software Construction and Development" */
    public String getDisplayName() {
        return code + " - " + name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}