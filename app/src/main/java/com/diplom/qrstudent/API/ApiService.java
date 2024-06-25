package com.diplom.qrstudent.API;
import com.diplom.qrstudent.Models.News;
import com.diplom.qrstudent.Models.Room;
import com.diplom.qrstudent.Models.TimeTable;
import com.diplom.qrstudent.Models.TurnstileHistory;
import com.diplom.qrstudent.Models.UserDTO;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/api/users/{id}")
    Call<UserDTO> getUserById(@Path("id") Long id);

    @GET("/api/timetable")
    Call<List<TimeTable>> getSchedule();
    @FormUrlEncoded
    @POST("/api/users/login")
    Call<Map<String, String>> loginUser(@Field("username") String username, @Field("password") String password);

    @PUT("/api/users/{id}")
    Call<UserDTO> updateUser(@Path("id") Long id, @Body UserDTO updatedUserDTO);

    @POST("/api/turnstile/scan/{userId}")
    Call<TurnstileHistory> scanUser(@Path("userId") long userId);

    @POST("/api/timetable/addStudentToTimetable/{timetableId}/{studentId}")
    Call<Void> addStudentToTimetable(
            @Path("timetableId") Long timetableId,
            @Path("studentId") Integer studentId
    );

    @Multipart
    @POST("/api/users/uploadImage/{id}")
    Call<Void> uploadImage(@Path("id") Integer id, @Part MultipartBody.Part image);

    @GET("/api/timetable/getStudents/{timetableId}")
    Call<List<Integer>> getStudentsForTimetable(@Path("timetableId") Long timetableId);

    @GET("/api/timetable/checkScanable/{timetableId}")
    Call<Boolean> checkTimetableScanable(@Path("timetableId") int timetableId);


    @GET("/api/rooms")
    Call<List<Room>> getFreeRooms();

    @GET("/api/news/")
    Call<List<News>> getAllNews();

    @GET("api/timetable/getSubjectNamesForGroup/{groupName}")
    Call<List<String>> getSubjectNamesForGroup(@Path("groupName") String groupName);

    @POST("api/users/saveFCMToken/{userId}")
    Call<Void> sendFcmToken(@Path("userId") long userId, @Query("fcmToken") String fcmToken);

    @GET("/api/guests")
    Call<List<UserDTO>> getGuests();
    @POST("/api/students/verifyguest")
    Call<Void> verifyGuest(@Query("guestId") Long guestId);

    @GET("/api/turnstile/getqrcode")
    Call<String> getDynamicQRCode();

}