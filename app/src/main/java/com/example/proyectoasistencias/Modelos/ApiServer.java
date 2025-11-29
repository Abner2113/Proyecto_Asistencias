package com.example.proyectoasistencias.Modelos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServer {

    @FormUrlEncoded
    @POST("LoginApp.php")
    Call<ApiResponse<List<Usuario>>> login(
            @Field("periodo") String periodo,
            @Field("puesto") String puesto,
            @Field("idTrabajador") String idTrabajador,
            @Field("contrasena") String contrasena
    );
    @FormUrlEncoded
    @POST("HistorialAsistenciasTrabajadorApp.php")
    Call<ApiResponse<java.util.List<Asistencia>>> obtenerAsistenciasTrabajador(
            @Field("idTrabajador") String idTrabajador,
            @Field("periodo") String periodo
    );
    @FormUrlEncoded
    @POST("HistorialIncidenciasTrabajadorApp.php")
    Call<ApiResponse<java.util.List<Incidencia>>> obtenerIncidenciasTrabajador(
            @Field("idTrabajador") String idTrabajador,
            @Field("periodo") String periodo
    );
    @FormUrlEncoded
    @POST("HorarioTrabajadorApp.php")
    Call<ApiResponse<java.util.List<Horario>>> obtenerHorarioTrabajador(
            @Field("idTrabajador") String idTrabajador,
            @Field("periodo") String periodo
    );
    @FormUrlEncoded
    @POST("LimpiarHistorialAsistenciasApp.php")
    Call<ApiResponse<Object>> limpiarHistorialAsistencias(
            @Field("dummy") String dummy
    );
    @FormUrlEncoded
    @POST("LimpiarHistorialIncidenciasApp.php")
    Call<ApiResponse<Object>> limpiarHistorialIncidencias(
            @Field("dummy") String dummy
    );
    @FormUrlEncoded
    @POST("ListaEmpleadosApp.php")
    Call<ApiResponse<java.util.List<Empleado>>> obtenerEmpleados(
            @Field("dummy") String dummy
    );
    @FormUrlEncoded
    @POST("ActualizarHorarioApp.php")
    Call<ApiResponse<Object>> actualizarHorarioTrabajador(
            @Field("idTrabajador") String idTrabajador,
            @Field("periodo") String periodo,
            @Field("dia") String dia,
            @Field("hEntrada") String hEntrada,
            @Field("hSalida") String hSalida
    );
    @GET("HistorialUsuariosApp.php")
    Call<ApiResponse<java.util.List<EmpleadoApp>>> obtenerUsuariosAdmin();

}