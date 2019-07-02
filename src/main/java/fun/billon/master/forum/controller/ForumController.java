package fun.billon.master.forum.controller;

import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.exception.ParamException;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.StringUtils;
import fun.billon.forum.api.feign.IForumService;
import fun.billon.forum.api.model.ForumPostModel;
import fun.billon.forum.api.model.ForumReplyModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 论坛模块相关接口
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/forum")
public class ForumController {

    /**
     * 内部服务id
     */
    @Value("${billon.master-web.sid}")
    private String sid;

    @Resource
    private IForumService forumService;

    /**
     * 发表帖子
     *
     * @param paramMap paramMap.expiredTime   过期时间(yyyy-MM-dd HH:mm:ss)              选填
     *                 paramMap.uid           用户id                                    必填
     *                 paramMap.title         标题                                      必填
     *                 paramMap.content       内容                                      必填
     *                 paramMap.lat           纬度(gps)                                 选填
     *                 paramMap.lng           经度(gps)                                 选填
     *                 paramMap.address       位置信息                                   选填
     *                 paramMap.topicId       话题id                                    必填
     *                 paramMap.status        帖子状态(1:审核中;2:审核通过;3:审核未通过)     选填
     *                 paramMap.limit         限制条件(1:免费观看;2:分享后可见;3:付费后可见)  选填
     *                 paramMap.price         价格                                       选填
     *                 paramMap.payment       支付方式(1:金币;2:现金)                      选填
     *                 paramMap.mediaList     附件                                       必填
     * @return
     */
    @PostMapping(value = "/post")
    public ResultModel<Integer> addPost(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"expiredTime", "uid", "title", "content", "lat", "lng", "address", "topicId",
                "status", "limit", "price", "payment", "mediaList"};
        boolean[] requiredArray = new boolean[]{false, true, true, true, false, false, false, true, false, false, false, false, true};
        Class[] classArray = new Class[]{Date.class, Integer.class, String.class, String.class, Double.class, Double.class,
                String.class, Integer.class, Integer.class, Integer.class, Float.class, Integer.class, String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.addPost(sid, sid, paramMap);
    }

    /**
     * 帖子评论
     *
     * @param postId   帖子id
     * @param paramMap paramMap.uid         用户id        必填
     *                 paramMap.content     内容          必填
     *                 paramMap.refId       管理评论id     选填
     * @return
     */
    @PostMapping(value = "/post/{postId}/reply")
    public ResultModel<Integer> addReply(@PathVariable(value = "postId") Integer postId,
                                         @RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        try {
            StringUtils.checkParam(paramMap, new String[]{"uid", "content", "refId"},
                    new boolean[]{true, true, false}, new Class[]{Integer.class, String.class, Integer.class}, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.addReply(sid, sid, postId, paramMap);
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子id 必填
     * @return
     */
    @DeleteMapping(value = "/post/{postId}")
    public ResultModel deletePost(@PathVariable(value = "postId") Integer postId) {
        return forumService.deletePost(sid, sid, postId, null);
    }

    /**
     * 删除帖子评论
     *
     * @param postId  帖子id  必填
     * @param replyId 评论id  必填
     * @return
     */
    @DeleteMapping(value = "/post/{postId}/reply/{replyId}")
    public ResultModel deleteReply(@PathVariable(value = "postId") Integer postId,
                                   @PathVariable(value = "replyId") Integer replyId) {
        return forumService.deleteReply(sid, sid, postId, replyId, null);
    }

    /**
     * 更新帖子
     *
     * @param postId   帖子id
     * @param paramMap paramMap.expiredTime     过期时间                                   选填
     *                 paramMap.title           标题                                      选填
     *                 paramMap.content         内容                                      选填
     *                 paramMap.statue          状态(1:审核中;2:审核通过;3:审核未通过)        选填
     *                 paramMap.limit           限制(1:免费观看;2:分享后可见;3:付费后可见)     选填
     *                 paramMap.price           价格                                      选填
     *                 paramMap.payment         支付方式(1:金币;2:现金)                     选填
     * @return
     */
    @PutMapping(value = "/post/{postId}")
    public ResultModel updatePost(@PathVariable(value = "postId") Integer postId,
                                  @RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"expiredTime", "title", "content", "status", "limit", "price", "payment"};
        boolean[] requiredArray = new boolean[]{false, false, false, false, false, false, false};
        Class[] classArray = new Class[]{Date.class, String.class, String.class,
                Integer.class, Integer.class, Double.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        return forumService.updatePost(sid, sid, postId, paramMap);
    }

    /**
     * 更新媒体文件
     *
     * @param postId   id
     * @param mediaId  附件id
     * @param paramMap paramMap.type           文件类型(1:图片;2:视频)                     选填
     *                 paramMap.cover          视频封面                                   选填
     *                 paramMap.url            文件地址                                   选填
     *                 paramMap.desc           描述                                      选填
     *                 paramMap.sort           排序                                      选填
     *                 paramMap.visiable       是否可见(1:可见;2:不可见)                   选填
     * @return
     */
    @PutMapping(value = "/post/{postId}/media/{mediaId}")
    public ResultModel updateMedia(@PathVariable(value = "postId") Integer postId,
                                   @PathVariable(value = "mediaId") Integer mediaId,
                                   @RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"type", "cover", "url", "desc", "sort", "visiable"};
        boolean[] requiredArray = new boolean[]{false, false, false, false, false, false};
        Class[] classArray = new Class[]{Integer.class, String.class, String.class,
                String.class, Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        return forumService.updateMedia(sid, sid, postId, mediaId, paramMap);
    }

    /**
     * 根据条件帖子数量
     *
     * @param paramMap paramMap.topicId     话题id          选填
     *                 paramMap.uid         要查看的用户id    选填
     */
    @GetMapping(value = "/post/count")
    public ResultModel<Integer> postCount(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"topicId", "uid"};
        boolean[] requiredArray = new boolean[]{false, false};
        Class[] classArray = new Class[]{Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.postCount(sid, sid, paramMap);
    }

    /**
     * 获取帖子列表
     *
     * @param paramMap paramMap.topicId    话题id           选填
     *                 paramMap.uid        要查看的用户id    选填
     *                 paramMap.pageIndex  分页页码         选填
     *                 paramMap.pageSize   分页大小         选填
     * @return
     */
    @GetMapping(value = "/post")
    public ResultModel<List<ForumPostModel>> posts(@RequestParam Map<String, String> paramMap) {
        ResultModel<List<ForumPostModel>> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"topicId", "uid", "pageIndex", "pageSize"};
        boolean[] requiredArray = new boolean[]{false, false, false, false};
        Class[] classArray = new Class[]{Integer.class, Integer.class, Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.posts(sid, sid, paramMap);
    }

    /**
     * 获取帖子详情
     *
     * @param postId   帖子id                                       必填
     * @param paramMap paramMap.requireExtend   是否需要帖子扩展信息     选填
     *                 paramMap.requireReplies  是否需要评论列表         选填
     *                 paramMap.requireUnlocks  是否需要解锁记录列表     选填
     * @return
     */
    @GetMapping(value = "/post/{postId}")
    public ResultModel<ForumPostModel> postDetail(@PathVariable(value = "postId") Integer postId,
                                                  @RequestParam Map<String, String> paramMap) {
        ResultModel<ForumPostModel> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"requireExtend", "requireReplies", "requireUnlocks"};
        boolean[] requiredArray = new boolean[]{false, false, false};
        Class[] classArray = new Class[]{Integer.class, Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.postDetail(sid, sid, postId, paramMap);
    }

    /**
     * 获取评论列表
     *
     * @param postId   帖子id                        必填
     * @param paramMap paramMap.pageIndex  分页页码   选填
     *                 paramMap.pageSize   分页大小   选填
     * @return
     */
    @GetMapping(value = "/post/{postId}/reply")
    public ResultModel<List<ForumReplyModel>> replies(@PathVariable(value = "postId") Integer postId,
                                                      @RequestParam Map<String, String> paramMap) {
        ResultModel<List<ForumReplyModel>> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"pageIndex", "pageSize"};
        boolean[] requiredArray = new boolean[]{false, false};
        Class[] classArray = new Class[]{Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }
        return forumService.replies(sid, sid, postId, paramMap);
    }

}