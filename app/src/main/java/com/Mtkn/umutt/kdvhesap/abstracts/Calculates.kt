package com.Mtkn.umutt.kdvhesap.abstracts

abstract class Calculates {

   //Toplam tutarın ve KDV tutarının hesaplandığı metotlar
   companion object {

      fun totalTutarDahil(tutar: Double?, kdvOran: Double?): Double? {

         return tutar?.div(1 + (kdvOran?.div(100) ?: 0.0))
      }

      fun totalTutarHaric(tutar: Double?, kdvOran: Double?): Double? {

         return tutar?.times(1 + (kdvOran?.div(100) ?: 0.0))
      }

      fun kdvTutarDahil(toplamTutar: Double?, kdvOran: Double?): Double? {

         return toplamTutar?.times(kdvOran?.div(100) ?: 0.0)
      }

      fun kdvTutarHaric(tutar: Double?, kdvOran: Double?): Double? {

         return tutar?.times(kdvOran?.div(100) ?: 0.0)
      }

   }
}
