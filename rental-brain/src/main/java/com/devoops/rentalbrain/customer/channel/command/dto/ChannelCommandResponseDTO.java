
package com.devoops.rentalbrain.customer.channel.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChannelCommandResponseDTO {

    private Long channelId;
    private String channelName;

}
