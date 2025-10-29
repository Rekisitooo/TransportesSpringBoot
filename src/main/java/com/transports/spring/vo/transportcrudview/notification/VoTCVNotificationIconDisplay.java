package com.transports.spring.vo.transportcrudview.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoTCVNotificationIconDisplay {
    private boolean showIcon;
    private String iconColor;

}
