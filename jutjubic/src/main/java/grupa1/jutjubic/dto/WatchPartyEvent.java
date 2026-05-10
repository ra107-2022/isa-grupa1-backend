package grupa1.jutjubic.dto;

public class WatchPartyEvent {

    private String type;
    private  String roomCode;
    private Long videoId;
    private Double timestamp;
    private Long clientTime;
    private Integer usersCount;

    public WatchPartyEvent(){}

    public WatchPartyEvent(String type, String roomCode, Long videoId, Double timestamp, Long clientTime, Integer usersCount){
        this.type = type;
        this.roomCode = roomCode;
        this.videoId = videoId;
        this.timestamp = timestamp;
        this.clientTime = clientTime;
        this.usersCount = usersCount;

    }

    public String getType(){

        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getRoomCode(){

        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Long getVideoId(){
        return  videoId;
    }

    public void setVideoId(Long videoId){
        this.videoId = videoId;
    }

    public Double getTimestamp() {
        return  timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }

    public Long getClientTime() {
        return clientTime;
    }
    public void setClientTime(Long clientTime) {
        this.clientTime = clientTime;
    }

    public Integer getUsersCount() {
        return usersCount;
    }
    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }
}
