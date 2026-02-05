package com.rentaherramientas.tolly.domain.model;

public class ToolImage {
    private Long id;
    private Long toolId;
    private String image_url;

    public ToolImage() {}

    public ToolImage(Long id, Long toolId, String image_url) {
        this.id = id;
        this.toolId = toolId;
        this.image_url = image_url;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getToolId() {return toolId;}
    public void setToolId(Long toolId) {this.toolId = toolId;}

    public String getimage_url() {return image_url;}
    public void setimage_url(String image_url) {this.image_url = image_url;}
}
