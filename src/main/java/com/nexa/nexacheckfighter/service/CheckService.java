package com.nexa.nexacheckfighter.service;

import com.google.gson.Gson;
import com.nexa.nexacheckfighter.dto.FighterDTO;
import com.nexa.nexacheckfighter.repository.FighterRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckService {
    private final FighterRepository fighterRepository;

    public CheckService(final FighterRepository fighterRepository) {
        this.fighterRepository = fighterRepository;
    }

    private void checkReset(final int id, final FighterDTO fighter, final List<FighterCheckLog> logs) {
        FighterDTO fighterOld = fighter;

        this.fighterRepository.updateFighterCharacteristic("constitution", id, 99);
        this.fighterRepository.updateFighterCharacteristic("dexterity", id, 99);
        this.fighterRepository.updateFighterCharacteristic("strength", id, 99);

        RestTemplate restTemplate = new RestTemplate();

        FighterDTO fighterNew = restTemplate.getForObject(
                fighter.url() + "/reset?id=" + id,
                FighterDTO.class
                                                         );
        if (fighterNew == null) {
            logs.add(new FighterCheckLog("Loose API check", false,
                                         "Fighter URL is not reachable or does not return a valid fighter after reset."));
        } else {
            logs.add(new FighterCheckLog("Loose API check", true,
                                         "Fighter URL is reachable and returns a valid fighter after reset."));
        }
        if (!fighterOld.equals(fighterNew)) {
            logs.add(new FighterCheckLog("Loose data consistency check", false,
                                         "Fighter data from URL after loose does not match local fighter data."));
        } else {
            logs.add(new FighterCheckLog("Loose data consistency check", true,
                                         "Fighter data from URL after loose matches local fighter data."));
        }
    }

    public String checkFighter(final int id) {
        List<FighterCheckLog> logs = new ArrayList<>();
        FighterDTO fighter = FighterRepository.toDTO(this.fighterRepository.findById(id));
        if (CheckService.checkFighterExists(id, fighter, logs)) return formatLogs(logs);
        CheckService.checkCharacteristics(fighter, logs);
        CheckService.checkXp(fighter, logs);
        CheckService.checkUrl(fighter, logs);
        if (fighter.url() == null || fighter.url().trim().equals("")) {
            return formatLogs(logs);
        }
        CheckService.checkPlayer(fighter, logs);
        CheckService.checkGetFighterAPI(id, fighter, logs);
        CheckService.checkLoose(id, fighter, logs);
        CheckService.checkWin(id, fighter, logs);

        return formatLogs(logs);
    }

    private static boolean checkFighterExists(final int id, final FighterDTO fighter,
                                              final List<FighterCheckLog> logs) {
        if (fighter == null) {
            logs.add(new FighterCheckLog("Fighter exists", false, "Fighter with ID " + id + " does not exist."));
            return true;
        }
        return false;
    }

    private String formatLogs(final List<FighterCheckLog> logs) {
        return new Gson().toJson(logs);
    }

    private static void checkCharacteristics(final FighterDTO fighter, final List<FighterCheckLog> logs) {
        if (fighter.constitution() < 3 || fighter.constitution() > 18) {
            logs.add(new FighterCheckLog("Constitution check", false,
                                         "Fighter's constitution must be between 3 and 18."));
        } else {
            logs.add(new FighterCheckLog("Constitution check", true, "Fighter's constitution is valid."));
        }
        if (fighter.dexterity() < 3 || fighter.dexterity() > 18) {
            logs.add(new FighterCheckLog("Dexterity check", false, "Fighter's dexterity must be between 3 and 18."));
        } else {
            logs.add(new FighterCheckLog("Dexterity check", true, "Fighter's dexterity is valid."));
        }
        if (fighter.strength() < 3 || fighter.strength() > 18) {
            logs.add(new FighterCheckLog("Strength check", false, "Fighter's strength must be between 3 and 18."));
        } else {
            logs.add(new FighterCheckLog("Strength check", true, "Fighter's strength is valid."));
        }
    }

    private static void checkXp(final FighterDTO fighter, final List<FighterCheckLog> logs) {
        if (fighter.xp() != 0) {
            logs.add(new FighterCheckLog("XP check", false, "Fighter's XP must be 0."));
        } else {
            logs.add(new FighterCheckLog("XP check", true, "Fighter's XP is valid."));
        }
    }

    private static void checkUrl(final FighterDTO fighter, final List<FighterCheckLog> logs) {
        if (fighter.url() == "") {
            logs.add(new FighterCheckLog("URL check", false, "Fighter's URL must not be empty."));
        } else {
            logs.add(new FighterCheckLog("URL check", true, "Fighter's URL is valid."));
        }
    }

    private static void checkPlayer(final FighterDTO fighter, final List<FighterCheckLog> logs) {
        if (fighter.player() == "") {
            logs.add(new FighterCheckLog("Player check", false, "Fighter's player must not be empty."));
        } else {
            logs.add(new FighterCheckLog("Player check", true, "Fighter's player is valid."));
        }
    }

    private static void checkGetFighterAPI(final int id, final FighterDTO fighter, final List<FighterCheckLog> logs) {
        RestTemplate restTemplate = new RestTemplate();

        FighterDTO fighterUrl = restTemplate.getForObject(
                fighter.url() + "/getFighter?id=" + id,
                FighterDTO.class
                                                         );
        if (fighterUrl == null) {
            logs.add(new FighterCheckLog("Fighter URL check", false,
                                         "Fighter URL is not reachable or does not return a valid fighter."));
        } else {
            logs.add(new FighterCheckLog("Fighter URL check", true,
                                         "Fighter URL is reachable and returns a valid fighter."));
        }
        if (!fighter.equals(fighterUrl)) {
            logs.add(new FighterCheckLog("Fighter data consistency check", false,
                                         "Fighter data from URL does not match local fighter data."));
        } else {
            logs.add(new FighterCheckLog("Fighter data consistency check", true,
                                         "Fighter data from URL matches local fighter data."));
        }
    }

    private static void checkLoose(final int id, final FighterDTO fighter, final List<FighterCheckLog> logs) {
        FighterDTO fighterOld = fighter;

        RestTemplate restTemplate = new RestTemplate();

        FighterDTO fighterNew = restTemplate.getForObject(
                fighter.url() + "/loose?id=" + id,
                FighterDTO.class
                                                         );
        if (fighterNew == null) {
            logs.add(new FighterCheckLog("Loose API check", false,
                                         "Fighter URL is not reachable or does not return a valid fighter after loose."));
        } else {
            logs.add(new FighterCheckLog("Loose API check", true,
                                         "Fighter URL is reachable and returns a valid fighter after loose."));
        }
        if (!fighterOld.equals(fighterNew)) {
            logs.add(new FighterCheckLog("Loose data consistency check", false,
                                         "Fighter data from URL after loose does not match local fighter data."));
        } else {
            logs.add(new FighterCheckLog("Loose data consistency check", true,
                                         "Fighter data from URL after loose matches local fighter data."));
        }
    }

    private static void checkWin(final int id, final FighterDTO fighter, final List<FighterCheckLog> logs) {
        FighterDTO fighterOld = fighter;

        RestTemplate restTemplate = new RestTemplate();

        FighterDTO fighterNew = restTemplate.getForObject(
                fighter.url() + "/win?id=" + id,
                FighterDTO.class
                                                         );
        if (fighterNew == null) {
            logs.add(new FighterCheckLog("Loose API check", false,
                                         "Fighter URL is not reachable or does not return a valid fighter after loose."));
        } else {
            logs.add(new FighterCheckLog("Loose API check", true,
                                         "Fighter URL is reachable and returns a valid fighter after loose."));
        }
        if ((fighterOld.strength() + 1 != fighterNew.strength()
                || fighterOld.dexterity() + 1 != fighterNew.dexterity()
                || fighterOld.constitution() + 1 != fighterNew.constitution())
                && (fighterOld.strength() + fighterOld.dexterity() + fighterOld.constitution() + 1
                != fighterNew.strength() + fighterNew.dexterity() + fighterNew.constitution())) {
            logs.add(new FighterCheckLog("Win data consistency check", false,
                                         "Fighter data from URL after win does not match local fighter data."));
        } else {
            logs.add(new FighterCheckLog("Win data consistency check", true,
                                         "Fighter data from URL after win matches local fighter data."));
        }
    }
}
