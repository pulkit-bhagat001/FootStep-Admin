package com.admin.shoppinappadmin.domain.models

data class Product(
    var name:String="",
    var desc:String="",
    var regularPrice:String="",
    var discountPercent:String="",
    var finalPrice:String="",
    var category:String="",
    var imageUrl: String="",
    var availableUnits:String="",
    var isAvailable:Boolean=true,
    var createdBy:String="",
)

data class ProductError(
    var nameError:Boolean=false,
    var regularPriceError:Boolean=false,
    var finalPriceError:Boolean=false,
    var availUnitsError:Boolean=false,
    var createdByError:Boolean=false,
    var imageError:Boolean=false
)
object Validator{
    fun validateName(name: String):Boolean{
        if(name.length<4){
            return false
        }else{
            return true
        }
    }

    fun validateRegularPrice(price: String):Boolean{
        val regex=Regex("[a-zA-Z]")
        return !regex.containsMatchIn(price)

    }
    fun validateFinalPrice(price: String):Boolean{
        val regex=Regex("[a-zA-Z]")
        return !regex.containsMatchIn(price)
    }
    fun validateAvailableUnits(units: String):Boolean{
        val regex=Regex("[a-zA-Z]")
        return !regex.containsMatchIn(units)
    }
    fun validateCreatedBy(name: String):Boolean{
        if(name.length<4){
            return false
        }else{
            return true
        }
    }
}
