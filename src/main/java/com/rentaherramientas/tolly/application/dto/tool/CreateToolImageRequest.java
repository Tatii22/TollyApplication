package com.rentaherramientas.tolly.application.dto.tool;

public class CreateToolImageRequest {
    private Long toolId;
    private String image_url;

    public CreateToolImageRequest() {}

    public CreateToolImageRequest(Long toolId, String image_url) {
        this.toolId = toolId;
        this.image_url = image_url;
    }

    public Long getToolId() {return toolId;}
    public void setToolId(Long toolId) {this.toolId = toolId;}

    public String getimage_url() {return image_url;}
    public void setimage_url(String image_url) {this.image_url = image_url;}
}
