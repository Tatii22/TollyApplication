package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tool_images")
public class ToolImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tool", nullable = false)
    private ToolEntity tool;

    @Column(name = "image_url", nullable = false, length = 255)
    private String image_url;

    public ToolImageEntity() {}

    public ToolImageEntity(Long id, ToolEntity tool, String image_url) {
        this.id = id;
        this.tool = tool;
        this.image_url = image_url;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public ToolEntity getTool() {return tool;}
    public void setTool(ToolEntity tool) {this.tool = tool;}

    public String getimage_url() {return image_url;}
    public void setimage_url(String image_url) {this.image_url = image_url;}
}
