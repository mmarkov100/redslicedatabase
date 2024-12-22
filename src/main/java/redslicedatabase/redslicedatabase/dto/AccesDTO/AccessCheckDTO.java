package redslicedatabase.redslicedatabase.dto.AccesDTO;

/*
DTO для получения uid файрбейза
 */

import jakarta.validation.constraints.NotNull;

public class AccessCheckDTO {

    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;

    // Геттеры и сеттеры
    public String getUidFirebase() {
        return uidFirebase;
    }
    public void setUidFirebase(String uidFirebase) {
        this.uidFirebase = uidFirebase;
    }
}
