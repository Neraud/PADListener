package fr.neraud.padlistener.http.parser.pad;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.constant.PADRegion;
import fr.neraud.padlistener.exception.UnknownMonsterException;
import fr.neraud.padlistener.helper.MonsterIdConverterHelper;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.CapturedPlayerInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.pad.constant.StartingColor;
import fr.neraud.padlistener.pad.model.GetPlayerDataApiCallResult;
import fr.neraud.padlistener.pad.model.PADCapturedFriendModel;

/**
 * JSON parser used to parse PAD GetPlayerData
 *
 * @author Neraud
 */
public class GetPlayerDataJsonParser extends AbstractJsonParser<GetPlayerDataApiCallResult> {

    private final PADRegion region;
    private final MonsterIdConverterHelper idConverter;

    public GetPlayerDataJsonParser(Context context, PADRegion region) {
        this.region = region;
        this.idConverter = new MonsterIdConverterHelper(context, region);
    }

    @Override
    protected GetPlayerDataApiCallResult parseJsonObject(JSONObject json) throws JSONException, ParsingException {
        MyLog.entry();

        final GetPlayerDataApiCallResult model = new GetPlayerDataApiCallResult();
        final int res = json.getInt("res");
        model.setRes(res);

        if (model.isResOk()) {
            final CapturedPlayerInfoModel playerInfo = new CapturedPlayerInfoModel();
            // "friendMax": 30, "cardMax": 30, "name": "NeraudMule", "lv": 19, "exp": 29209, "cost": 32, "sta": 26, "sta_max": 26, "gold": 5, "coin": 63468, "curLvExp": 27188, "nextLvExp": 30954,
            playerInfo.setLastUpdate(new Date());
            playerInfo.setFriendMax(json.getInt("friendMax"));
            playerInfo.setCardMax(json.getInt("cardMax"));
            playerInfo.setName(json.getString("name"));
            playerInfo.setRank(json.getInt("lv"));
            playerInfo.setExp(json.getLong("exp"));
            playerInfo.setCostMax(json.getInt("cost"));
            playerInfo.setStamina(json.getInt("sta"));
            playerInfo.setStaminaMax(json.getInt("sta_max"));
            playerInfo.setStones(json.getInt("gold"));
            playerInfo.setCoins(json.getLong("coin"));
            playerInfo.setCurrentLevelExp(json.getLong("curLvExp"));
            playerInfo.setNextLevelExp(json.getLong("nextLvExp"));
            playerInfo.setRegion(region);

            model.setPlayerInfo(playerInfo);

            // "card"
            final List<MonsterModel> monsters = new ArrayList<MonsterModel>();
            final JSONArray cardResults = json.getJSONArray("card");
            for (int i = 0; i < cardResults.length(); i++) {
                MyLog.info(" - card #" + i + " : " + cardResults.get(i));
                final Object cardResult = cardResults.get(i);

                final MonsterModel monster;
                if (cardResult instanceof JSONObject) {
                    monster = parseMonsterFromObject((JSONObject) cardResult);
                } else {
                    monster = parseMonsterFromArray((JSONArray) cardResult);
                }

                monsters.add(monster);
            }
            model.setMonsterCards(monsters);

            // "friends"
            final List<PADCapturedFriendModel> friends = new ArrayList<PADCapturedFriendModel>();
            final JSONArray friendResults = json.getJSONArray("friends");
            for (int i = 0; i < friendResults.length(); i++) {
                final JSONArray friendResult = (JSONArray) friendResults.get(i);
                final PADCapturedFriendModel friend = parseFriend(friendResult);

                friends.add(friend);
            }
            model.setFriends(friends);
        }

        MyLog.exit();
        return model;
    }

    private MonsterModel parseMonsterFromObject(final JSONObject cardResult) throws JSONException {
        MyLog.entry();

        //"cuid": 1, "exp": 15939, "lv": 16, "slv": 1, "mcnt": 11, "no": 3, "plus": [0, 0, 0, 0]
        final MonsterModel monster = new MonsterModel();
        monster.setExp(cardResult.getLong("exp"));
        monster.setLevel(cardResult.getInt("lv"));
        monster.setSkillLevel(cardResult.getInt("slv"));
        final int origId = cardResult.getInt("no");
        int idJp = -1;
        try {
            idJp = idConverter.getMonsterRefIdByCapturedId(origId);
        } catch (UnknownMonsterException e) {
            MyLog.warn(e.getMessage());
        }
        monster.setIdJp(idJp);
        final JSONArray plusResults = cardResult.getJSONArray("plus");
        monster.setPlusHp(plusResults.getInt(0));
        monster.setPlusAtk(plusResults.getInt(1));
        monster.setPlusRcv(plusResults.getInt(2));
        monster.setAwakenings(plusResults.getInt(3));

        MyLog.exit();
        return monster;
    }

    private MonsterModel parseMonsterFromArray(final JSONArray cardResult) throws JSONException {
        MyLog.entry();

        //[8,190542,30,1,0,1655,0,0,0,0,0]
        final MonsterModel monster = new MonsterModel();
        monster.setCardId(cardResult.getLong(0));
        monster.setExp(cardResult.getLong(1));
        monster.setLevel(cardResult.getInt(2));
        monster.setSkillLevel(cardResult.getInt(3));
        final int origId = cardResult.getInt(5);
        int idJp = -1;
        try {
            idJp = idConverter.getMonsterRefIdByCapturedId(origId);
        } catch (UnknownMonsterException e) {
            MyLog.warn(e.getMessage());
        }
        monster.setIdJp(idJp);
        //TODO not sure about these ... what's the #10 field for ?!
        monster.setPlusHp(cardResult.getInt(6));
        monster.setPlusAtk(cardResult.getInt(7));
        monster.setPlusRcv(cardResult.getInt(8));
        monster.setAwakenings(cardResult.getInt(9));

        MyLog.exit();
        return monster;
    }


    private PADCapturedFriendModel parseFriend(final JSONArray friendResult) throws JSONException {
        MyLog.entry();

        //[4, 333300602, "NeraudMule", 17, 1, "140829151957", 9, 29, 6, 1, 0, 0, 0, 0, 2, 15, 1, 0, 0, 0, 0, 2, 15, 1, 0, 0, 0, 0]
        // New [5, 329993422, "HFR|Neraud", 292, 1, "150613152858", 48, 50, 6, 1, 0, 0, 0, 0, 0, 1422, 99, 1, 13, 12, 9, 6, 0, 1217, 99, 6, 99, 99, 99, 6, 0]
        final PADCapturedFriendModel friend = new PADCapturedFriendModel();
        friend.setId(friendResult.getLong(1));
        friend.setName(friendResult.getString(2));
        friend.setRank(friendResult.getInt(3));
        friend.setStartingColor(StartingColor.valueByCode(friendResult.getInt(4)));
        final String lastActivityDateString = friendResult.getString(5);
        try {
            final DateFormat parseFormat = new SimpleDateFormat("yyMMddHHmmss");
            parseFormat.setTimeZone(region.getTimeZone());
            final Date lastActivityDate = parseFormat.parse(lastActivityDateString);
            friend.setLastActivityDate(lastActivityDate);
        } catch (ParseException e) {
            MyLog.warn("error parsing lastActivityDate : " + e.getMessage());
        }

        final BaseMonsterStatsModel leader1 = extractFriendLeader(friendResult, 16);
        friend.setLeader1(leader1);

        final BaseMonsterStatsModel leader2 = extractFriendLeader(friendResult, 24);
        friend.setLeader2(leader2);

        MyLog.exit();
        return friend;
    }

    private BaseMonsterStatsModel extractFriendLeader(final JSONArray friendResult, int startPosition) throws JSONException {
        final BaseMonsterStatsModel leader = new BaseMonsterStatsModel();

        // 2, 15, 1, 0, 0, 0, 0
        final int origId1 = friendResult.getInt(startPosition++);
        int idJp1 = -1;
        try {
            idJp1 = idConverter.getMonsterRefIdByCapturedId(origId1);
        } catch (UnknownMonsterException e) {
            MyLog.warn("leader : " + e.getMessage());
        }

        leader.setIdJp(idJp1);
        leader.setLevel(friendResult.getInt(startPosition++));
        leader.setSkillLevel(friendResult.getInt(startPosition++));
        leader.setPlusHp(friendResult.getInt(startPosition++));
        leader.setPlusAtk(friendResult.getInt(startPosition++));
        leader.setPlusRcv(friendResult.getInt(startPosition++));
        leader.setAwakenings(friendResult.getInt(startPosition++));
        return leader;
    }

    @Override
    protected GetPlayerDataApiCallResult parseJsonArray(JSONArray json) throws JSONException, ParsingException {
        throw new ParsingException("Cannot parse JSONArray, JSONObject expected");
    }

}

