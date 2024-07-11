package com.maintainapps.modules.util;

import org.springframework.stereotype.Component;

@Component
public class ResponseVO
{
   private String status;
   private String response;
   private int code;

   protected ResponseVO()
   {
   }

   public ResponseVO(String status, String response, int code)
   {
      super();
      this.status = status;
      this.response = response;
      this.code = code;
   }

   public String getStatus()
   {
      return status;
   }

   public void setStatus(String status)
   {
      this.status = status;
   }

   public String getResponse()
   {
      return response;
   }

   public void setResponse(String response)
   {
      this.response = response;
   }

   public int getCode()
   {
      return code;
   }

   public void setCode(int code)
   {
      this.code = code;
   }
}
