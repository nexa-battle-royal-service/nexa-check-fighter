package com.nexa.nexacheckfighter.dto;

public record FighterDTO(
        int id,
        String name,
        int constitution,
        int strength,
        int dexterity,
        int xp,
        String url,
        String player) {
}
