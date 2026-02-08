package com.example.kaisi_lagi.PeopleMaster;

public class PeopleDTO {

    private Long peopleId;
    private String peopleName;
    private String image; // Base64 string of image
    private Double avgRating; // optional, if needed

    // Default constructor
    public PeopleDTO() {}

    // Parameterized constructor
    public PeopleDTO(Long peopleId, String peopleName, String image, Double avgRating) {
        this.peopleId = peopleId;
        this.peopleName = peopleName;
        this.image = image;
        this.avgRating = avgRating;
    }


    public Long getPeopleId() { return peopleId; }
    public void setPeopleId(Long peopleId) { this.peopleId = peopleId; }

    public String getPeopleName() { return peopleName; }
    public void setPeopleName(String peopleName) { this.peopleName = peopleName; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Double getAvgRating() { return avgRating; }
    public void setAvgRating(Double avgRating) { this.avgRating = avgRating; }
}
