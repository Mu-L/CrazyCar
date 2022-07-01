package com.tastsong.crazycar.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tastsong.crazycar.mapper.TimeTrialMapper;
import com.tastsong.crazycar.mapper.UserMapper;
import com.tastsong.crazycar.model.TimeTrialInfoModel;
import com.tastsong.crazycar.model.TimeTrialRankModel;

@Service
public class TimeTrialService {
    @Autowired
    private TimeTrialMapper timeTrialMapper;

    @Autowired
    private UserMapper userMapper;

    private void initRank(Integer cid){
        timeTrialMapper.delTimeTrialRank(cid);
        timeTrialMapper.initTimeTrialRank(cid);
    }

    public List<TimeTrialRankModel> getRankList(Integer cid){
        initRank(cid);
        List<TimeTrialRankModel> timeTrialRankModels =  timeTrialMapper.getTimeTrialRank(cid);
        for (Integer i = 0; i< timeTrialRankModels.size(); i++){
            Integer uid = timeTrialRankModels.get(i).uid;
            timeTrialRankModels.get(i).aid = userMapper.getUserByUid(uid).aid;
            timeTrialRankModels.get(i).user_name = userMapper.getUserByUid(uid).user_name;
        }
        return timeTrialRankModels;
    }

    public List<TimeTrialInfoModel> getTimeTrialDetail(Integer uid){
        List<TimeTrialInfoModel> timeTrialInfoModels = timeTrialMapper.getTimeTrialInfo();
        for(Integer i = 0; i < timeTrialInfoModels.size(); i++){
            timeTrialInfoModels.get(i).is_has = timeTrialMapper.isHasTimeTrialClass(uid, timeTrialInfoModels.get(i).cid);
        }
        return timeTrialInfoModels;
    }
}