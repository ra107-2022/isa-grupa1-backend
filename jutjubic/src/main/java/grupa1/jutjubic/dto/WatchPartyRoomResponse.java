package grupa1.jutjubic.dto;

public class WatchPartyRoomResponse {

    private String code;
    private  Long ownerId;

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public Long getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(Long ownerId){
        this.ownerId = ownerId;
    }
}
