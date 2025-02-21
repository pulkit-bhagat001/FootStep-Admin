package com.admin.shoppinappadmin.presentation.AdminViewModel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.admin.shoppinappadmin.common.PageEvent
import com.admin.shoppinappadmin.common.SubmitEvent
import com.admin.shoppinappadmin.domain.Repository.Repository
import com.admin.shoppinappadmin.domain.models.Product
import com.admin.shoppinappadmin.domain.models.ProductError
import com.admin.shoppinappadmin.domain.models.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(private val application: Application, private val repo: Repository) : AndroidViewModel(application) {

    //Product Screen
    private val _productPageState = MutableStateFlow(PageState())
    val productPageState = _productPageState.asStateFlow()

    private val _productPageTextField = MutableStateFlow(Product())
    val productPageTextField = _productPageTextField.asStateFlow()

    private val _productPageTextFieldError = MutableStateFlow(ProductError())
    val productPageTextFieldError = _productPageTextFieldError.asStateFlow()

    private val _finalError = MutableStateFlow(false)
    val finalError = _finalError.asStateFlow()

    private var imageUri: Uri? by mutableStateOf(null)

    fun productPageTextFieldChanged(field: String, value: String) {
        _productPageTextField.value = when (field) {
            "name" -> _productPageTextField.value.copy(
                name = value
            )

            "desc" -> _productPageTextField.value.copy(
                desc = value
            )

            "rp" -> _productPageTextField.value.copy(
                regularPrice = value
            )

            "fp" -> _productPageTextField.value.copy(
                finalPrice = value
            )

            "cat" -> _productPageTextField.value.copy(
                category = value
            )

            "availUnit" -> _productPageTextField.value.copy(
                availableUnits = value
            )

            else -> {
                _productPageTextField.value.copy(createdBy = value)
            }
        }
        _productPageTextFieldError.value = _productPageTextFieldError.value.copy(
            nameError = Validator.validateName(_productPageTextField.value.name),
            regularPriceError = Validator.validateRegularPrice(_productPageTextField.value.regularPrice),
            finalPriceError = Validator.validateFinalPrice(_productPageTextField.value.finalPrice),
            availUnitsError = Validator.validateAvailableUnits(_productPageTextField.value.availableUnits),
            createdByError = Validator.validateCreatedBy(_productPageTextField.value.createdBy)
        )
        validateFinal()

    }
    fun validateFinal(){
        if ((_productPageTextFieldError.value.nameError && _productPageTextFieldError.value.regularPriceError && _productPageTextFieldError.value.finalPriceError && _productPageTextFieldError.value.availUnitsError && _productPageTextFieldError.value.createdByError) && imageUri != null) {
            _finalError.value = true
        } else {
            _finalError.value = false
        }
    }

    fun submitEvent(event: SubmitEvent) {
        when (event) {
            SubmitEvent.Submit -> {
                if (_finalError.value) {
                    combinedState(imageUri!!,application.contentResolver,"image","newPreset")

                }
            }
        }
    }

    fun combinedState(
        imageUri: Uri,
        contentResolver: ContentResolver, folderName: String, uploadPreset: String
    ){ viewModelScope.launch {
        repo.addProductImage(imageUri,contentResolver,folderName,uploadPreset).collectLatest {
            when(it){
                is PageEvent.Error -> {
                    _productPageState.value=_productPageState.value.copy(
                        imageError = it.error,
                        loading = false
                    )
                }
                PageEvent.Loading -> { _productPageState.value=_productPageState.value.copy(
                    loadingImage = true
                )}
                is PageEvent.Success -> {
                    _productPageState.value=_productPageState.value.copy(
                        loadingImage = false
                    )
                    val discount=((_productPageTextField.value.regularPrice.toFloat()-_productPageTextField.value.finalPrice.toFloat())/_productPageTextField.value.regularPrice.toFloat())*100


                        _productPageTextField.value = _productPageTextField.value.copy(
                            imageUrl = it.success,
                            discountPercent = discount.toString()
                        )

                        repo.addProduct(_productPageTextField.value).collectLatest { res ->
                            when (res) {
                                is PageEvent.Error -> {
                                    _productPageState.value = _productPageState.value.copy(
                                        productError = res.error,
                                        loading = false
                                    )
                                }

                                PageEvent.Loading -> {
                                    _productPageState.value = _productPageState.value.copy(
                                        loading = true
                                    )
                                }

                                is PageEvent.Success -> {
                                    _productPageState.value = _productPageState.value.copy(
                                        success = res.success,
                                        loading = false
                                    )


                                }
                            }
                        }


                }
            }

        }
    }

    }
    fun clearAll(){
        imageUri=null
        _productPageState.value=PageState()
        _productPageTextField.value=Product()
        _productPageTextFieldError.value=ProductError()
        _finalError.value=false

    }



    fun getImageUri(uri: Uri?) {
        if (uri == null) {
            imageUri = null
        } else {
            imageUri = uri
        }
    }


}

data class PageState(
    val loadingImage:Boolean=false,
    val success: String = "",
    val imageError: String = "",
    val loading: Boolean = false,
    val productError:String=""
)
