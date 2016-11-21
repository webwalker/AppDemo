//package com.webwalker.framework.http;
//
//import com.ymatou.shop.reconstract.base.constants.UrlConstants;
//import com.ymatou.shop.reconstract.live.model.ProductDetailResult;
//import com.ymatou.shop.reconstract.mine.attention.model.AttentionCategoryListEntity;
//import com.ymatou.shop.reconstract.mine.attention.model.AttentionSubjectListEntity;
//import com.ymatou.shop.reconstract.mine.attention.model.AttentionTopicListResult;
//import com.ymatou.shop.reconstract.mine.attention.model.AttentionUserListEntity;
//import com.ymatou.shop.reconstract.mine.attention.model.GuestAttentionBasicItem;
//import com.ymatou.shop.reconstract.mine.collect.model.CollectDiaryResult;
//import com.ymatou.shop.reconstract.mine.collect.model.CollectedProductListEntity;
//import com.ymatou.shop.reconstract.mine.collect.model.GuessDiaryResult;
//import com.ymatou.shop.reconstract.mine.collect.model.OperationResultEntity;
//import com.ymatou.shop.reconstract.mine.model.AccountInfoDataResult;
//import com.ymatou.shop.reconstract.mine.model.MineDiaryDataResult;
//import com.ymatou.shop.reconstract.mine.topic.model.TopicEntity;
//import com.ymatou.shop.reconstract.mine.topic.model.TopicItemsEntity;
//import com.ymatou.shop.reconstract.mine.topic.model.TopicListEntity;
//import com.ymt.framework.http.model.BaseResult;
//import com.ymt.framework.http.model.NewBaseResult;
//
//import java.util.Map;
//
//import okhttp3.RequestBody;
//import retrofit2.Response;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.HTTP;
//import retrofit2.http.HeaderMap;
//import retrofit2.http.POST;
//import retrofit2.http.QueryMap;
//import retrofit2.http.Url;
//import rx.Observable;
//
///**
// * 非基础性数据接口
// * <p>
// * 如不通过map传递参数，可以在@GET中定义变量，传参替换
// * Created by xujian on 2016/11/8.
// */
//public interface UcDataService {
//    //-----------笔记相关----------------//
//    @GET(UrlConstants.URL_GET_COLLECTED_REQUEST_NOTES)
//    Observable<Response<CollectDiaryResult>> getFavoriteDiarys(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_GET_COLLECT_GUESS_NOTES)
//    Observable<Response<GuessDiaryResult>> getGuessDiarys(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_USER_DIARY_LIST)
//    Observable<Response<MineDiaryDataResult>> getMyDiaryList(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //-----------清单相关----------------//
//    @GET(UrlConstants.URL_GET_COLLECT_GUESS_TOPIC)
//    Observable<Response<AttentionTopicListResult>> getGuessTopics(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_GET_TOPIC_TOPIC_SIMPLE_INFO_LIST)
//    Observable<Response<TopicListEntity>> getTopicSimpleInfoList(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @POST(UrlConstants.URL_POST_MINE_TOPIC_ADD_ITEMS)
//    Observable<Response<OperationResultEntity>> postAddItemsToTopic(@HeaderMap Map<String, String> map, @Body RequestBody body);
//
//    @GET(UrlConstants.URL_GET_MINE_TOPIC_SUMMARY)
//    Observable<Response<TopicEntity>> getTopicSummary(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_GET_MINE_TOPIC_ITEMS)
//    Observable<Response<TopicItemsEntity>> getTopicItems(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_GET_MINE_TOPIC_LIST)
//    Observable<Response<TopicListEntity>> getUserTopicList(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_get_ATTENTION_TOPIC_LIST)
//    Observable<Response<AttentionTopicListResult>> getAttentionTopics(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_get_GUEST_ATTENTION_LIST)
//    Observable<Response<GuestAttentionBasicItem>> getGuestAttentionTopics(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //-----------产品相关----------------//
//    @GET(UrlConstants.URL_GET_COLLECTED_REQUEST_PRODUCTS)
//    Observable<Response<CollectedProductListEntity>> requestCollectedProducts(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //------------关注-----------------//
//    @GET(UrlConstants.URL_get_ATTENTION_USER_LIST)
//    Observable<Response<AttentionUserListEntity>> getAttentionUsers(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_get_ATTENTION_BRAND_LIST)
//    Observable<Response<AttentionCategoryListEntity>> getAttentionBrands(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_get_ATTENTION_CATEGORIES_LIST)
//    Observable<Response<AttentionCategoryListEntity>> getAttentionCategories(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    @GET(UrlConstants.URL_get_ATTENTION_SUBJECT_LIST)
//    Observable<Response<AttentionSubjectListEntity>> getAttentionSubjects(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //用户
//    @POST(UrlConstants.URL_USER_INFO)
//    Observable<Response<AccountInfoDataResult>> requestUserInfo(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //足迹
//    @GET(UrlConstants.URL_TRACK_PRODUCT_DETAIL_BY_IDS)
//    Observable<Response<ProductDetailResult>> requestTrackProductDetailByIds(@QueryMap Map<String, String> param, @HeaderMap Map<String, String> map);
//
//    //-----------通用----------------//
//    @HTTP(method = "DELETE", hasBody = true)
//    Observable<Response<NewBaseResult>> delete(@Url String url, @HeaderMap Map<String, String> map, @Body RequestBody body);
//
//    @HTTP(method = "DELETE", hasBody = true)
//    Observable<Response<BaseResult>> deleteOld(@Url String url, @HeaderMap Map<String, String> map, @Body RequestBody body);
//}
