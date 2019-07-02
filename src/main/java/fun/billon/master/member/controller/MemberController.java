package fun.billon.master.member.controller;

import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.exception.ParamException;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.StringUtils;
import fun.billon.member.api.feign.IMemberService;
import fun.billon.member.api.model.AdminModel;
import fun.billon.member.api.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户模块相关接口
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    /**
     * 内部服务id
     */
    @Value("${billon.master-web.sid}")
    private String sid;

    /**
     * member
     */
    @Autowired
    private IMemberService memberService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "/admin")
    public ResultModel<AdminModel> admin(@RequestAttribute(value = "uid") Integer uid) {
        return memberService.getAdminById(sid, sid, uid);
    }

    /**
     * 根据条件获取用户数
     *
     * @return
     */
    @GetMapping(value = "/user/count")
    public ResultModel<Integer> userCount(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"query"};
        boolean[] requiredArray = new boolean[]{false};
        Class[] classArray = new Class[]{String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return memberService.userCount(sid, sid, paramMap);
    }

    /**
     * 搜索用户
     *
     * @return
     */
    @GetMapping(value = "/user")
    public ResultModel<List<UserModel>> searchUser(@RequestParam Map<String, String> paramMap) {
        ResultModel<List<UserModel>> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"query", "pageSize", "pageIndex"};
        boolean[] requiredArray = new boolean[]{false, false, false};
        Class[] classArray = new Class[]{String.class, Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return memberService.search(sid, sid, paramMap);
    }

}
