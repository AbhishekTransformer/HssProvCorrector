package com.tcs.parser.main;

import com.tcs.corrector.service.FixResult;
import com.tcs.exceptions.BadRequestException;
import com.tcs.fixer.http.FixApiResponse;
import com.tcs.fixer.http.FixRequest;
import com.tcs.fixer.http.RequestValidator;
import com.tcs.parser.controller.HSSProvControllerImpl;
import com.tcs.parser.utils.ConfigUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/")
public class FixApi {
    private static Logger LISTENER_LOGGER = LogManager.getLogger(FixApi.class);

    @POST
    @Path("/profileFix")
    @Operation(summary = "Fix User",
            tags = {"fix"},
            description = "Start fixing for the input user",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = FixRequest.class), mediaType = "application/json")),
            responses = {
                    @ApiResponse(description = "Fix Success", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = FixApiResponse.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid User Id"),
                    @ApiResponse(responseCode = "500", description = "Processing Error")
            })
    @Produces(MediaType.APPLICATION_JSON)
    public Response fixProfile(FixRequest request) {
    	//---------------------Extra sleep time--------------------
    	try {
			Thread.sleep(3000);
			LISTENER_LOGGER.info("3 seconds sleep....");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			LISTENER_LOGGER.info("In catch....");
			e1.printStackTrace();
		}
    	//--------------------------------------------------------
        FixApiResponse response;
        String userId = request.getUserId(), source = request.getSource();
        System.out.println(request+" user Id "+ userId);
        userId = userId.trim();// added
//        System.out.println(request+"user Id"+ userId);
        LISTENER_LOGGER.info("User Id: after strip "+ userId);
        try {
            RequestValidator validator = new RequestValidator();
            validator.validateEntity("userId", userId);
            validator.validateEntity("source", source);
            HSSProvControllerImpl controller = new HSSProvControllerImpl();
            response = controller.validateAndFix(userId, source);
            ConfigUtils.storeIndexList.clear();
            LISTENER_LOGGER.info("Index List at end: "+ ConfigUtils.storeIndexList.toString());
            LISTENER_LOGGER.info("200 Error response for userId : " + userId + " status : " + response.getFixStatus() + " reason : " + response.getReason()); 
            return Response.status(Status.OK).entity(response).build();

        } catch (BadRequestException ex) {
            LISTENER_LOGGER.info("400 Error response " + userId);
            response = new FixApiResponse();
            response.setUserId(userId);
            response.setFixStatus(FixResult.FAILED_MANUAL.str());
            response.setReason(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            LISTENER_LOGGER.info("IN exception catch block " + ConfigUtils.getStackTraceString(e));
            LISTENER_LOGGER.info("500 Error response " + userId);
            response = new FixApiResponse();
            response.setUserId(userId);
            response.setFixStatus(FixResult.FAILED_MANUAL.str());
            response.setReason("Processing Error Reason : " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
        } finally {
            LISTENER_LOGGER.info("API process finished. In API finally block.");
        }
    }

    @GET
    @Path("/isUp")
    @Produces(MediaType.TEXT_PLAIN)
    public String isUp() {
        LISTENER_LOGGER.info("isUp");
        return "Up and Running !";
    }
}
