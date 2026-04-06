package com.transports.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COLOR")
public final class Color {

    @Id
    private Integer id;

    @Column(name = "ROJO_MODO_CLARO")
    private Integer redLightMode;

    @Column(name = "VERDE_MODO_CLARO")
    private Integer greenLightMode;

    @Column(name = "AZUL_MODO_CLARO")
    private Integer blueLightMode;

    @Column(name = "ROJO_MODO_OSCURO")
    private Integer redDarkMode;

    @Column(name = "VERDE_MODO_OSCURO")
    private Integer greenDarkMode;

    @Column(name = "AZUL_MODO_OSCURO")
    private Integer blueDarkMode;

    /**
     * Returns the RGB color in light mode as a string in the format "rgb(red, green, blue)".
     * @return the RGB color in light mode
     */
    public String getLightModeRGB() {
        return "rgb(" + this.redLightMode + ", " + this.greenLightMode + ", " + this.blueLightMode + ")";
    }
}
