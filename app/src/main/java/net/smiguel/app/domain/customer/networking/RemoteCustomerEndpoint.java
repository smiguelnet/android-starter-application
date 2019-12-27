package net.smiguel.app.domain.customer.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemoteCustomerEndpoint {

    String ENDPOINT_OPERATION = "customers";

    @GET(ENDPOINT_OPERATION)
    Call<CustomerDataList> getAll();

    @GET(ENDPOINT_OPERATION + "/{id}")
    Call<CustomerData> get(@Path("id") Long id);

    @DELETE(ENDPOINT_OPERATION + "/{id}")
    Call<CustomerData> delete(@Path("id") Long id);

    @POST(ENDPOINT_OPERATION)
    Call<CustomerData> create(@Body CustomerData customerData);

    @PUT(ENDPOINT_OPERATION)
    Call<CustomerData> update(@Body CustomerData customerData);

    class CustomerDataList {
        @Expose
        @SerializedName("customers")
        List<CustomerData> customers;
    }

    class CustomerData {

        public CustomerData(Long idRef, Date insertDate, Date updateDate, boolean active, String name, String email, Date birthday) {
            this.idRef = idRef;
            this.insertDate = insertDate;
            this.updateDate = updateDate;
            this.active = active;
            this.name = name;
            this.email = email;
            this.birthday = birthday;
        }

        @Expose
        @SerializedName("id")
        Long idRef;

        @Expose
        @SerializedName("insert_date")
        Date insertDate;

        @Expose
        @SerializedName("update_date")
        Date updateDate;

        @Expose
        @SerializedName("active")
        boolean active;
        //endregion

        @Expose
        @SerializedName("name")
        String name;

        @Expose
        @SerializedName("email")
        String email;

        @Expose
        @SerializedName("birthday")
        Date birthday;
    }
}
