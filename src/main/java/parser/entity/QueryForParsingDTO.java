package parser.entity;

public class QueryForParsingDTO {
    private String realtyType;
    private String city;
    private String description;
    private String nameOfFile;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameOfFile() {
        return nameOfFile;
    }

    public void setNameOfFile(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRealtyType() {
        return realtyType;
    }

    public void setRealtyType(String realtyType) {
        this.realtyType = realtyType;
    }
}
