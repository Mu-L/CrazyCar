package com.tastsong.crazycar.controller;

import com.tastsong.crazycar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastsong.crazycar.service.EquipService;
import com.tastsong.crazycar.utils.Util;
import com.tastsong.crazycar.common.Result;
import com.tastsong.crazycar.common.ResultCode;

import cn.hutool.json.JSONObject;

@RestController
@Scope("prototype")
@RequestMapping(value = "/v2/Equip")
public class EquipController {
    @Autowired
    private EquipService equipService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/Detail")
    public Object getEquipDetail(@RequestHeader(Util.TOKEN) String token) throws Exception{
        int uid = Util.getUidByToken(token);
        JSONObject data = new JSONObject();
        data.putOpt("equips", equipService.getEquipDetail(uid));
        data.putOpt("curEid", userService.getUserByUid(uid).getEid());
        return data;
    }

    @PostMapping(value = "/Buy")
    public Object buyEquip(@RequestHeader(Util.TOKEN) String token, @RequestBody JSONObject body) throws Exception{
		JSONObject data = new JSONObject();
		if (body != null && body.containsKey("eid")) {
            int uid = Util.getUidByToken(token);
			int eid = body.getInt("eid");
			if (equipService.hasEquip(uid, eid)) {
				data.putOpt("star", userService.getUserStar(uid));
                return data;
			} else if (equipService.canBuyEquip(uid, eid)) {
				equipService.bugEquip(uid, eid);
				data.putOpt("star", userService.getUserStar(uid));
                return data;
			} else {
                return Result.failure(ResultCode.RC423);
			}
		} else {
            return Result.failure(ResultCode.RC404);
		}			
    }

    @PostMapping(value = "/Change")
    public Object changeEquip(@RequestHeader(Util.TOKEN) String token, @RequestBody JSONObject body) throws Exception{
        int uid = Util.getUidByToken(token);
        if (body != null && body.containsKey("eid")) {
			int eid = body.getInt("eid");
			if (equipService.hasEquip(uid, eid)) {
				equipService.changeEquip(uid, eid);
                JSONObject data = new JSONObject();
                data.putOpt("eid", eid);
                return data;
			} else {
                return Result.failure(ResultCode.RC423);
			}
		} else {
            return Result.failure(ResultCode.RC404);
		}	
    }
}
