package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.UploadEventDto;

public interface IUploadEventProducerService {
    public void sendJsonEvent(UploadEventDto event);
    public void sendProtoEvent(UploadEventDto event);
}
