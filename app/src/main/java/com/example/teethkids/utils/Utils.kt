package com.example.teethkids.utils


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.teethkids.R
import com.example.teethkids.model.Address
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.Timestamp
import com.google.firebase.auth.*
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.GeocodingResult
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
object Utils {

    private lateinit var storageReference: StorageReference


    // Exibe um toast .
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    //  Calcula a distância entre duas coordenadas geográficas.
    fun calculateDistance(geoPoint1: GeoPoint, geoPoint2: GeoPoint): Double {
        val raio = 6371
        val lat1 = geoPoint1.latitude
        val lon1 = geoPoint1.longitude
        val lat2 = geoPoint2.latitude
        val lon2 = geoPoint2.longitude

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Radians = Math.toRadians(lat1)
        val lat2Radians = Math.toRadians(lat2)

        val a =
            sin(dLat / 2) * sin(dLat / 2) + sin(dLon / 2) * sin(dLon / 2) * cos(lat1Radians) * cos(
                lat2Radians
            )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return raio * c
    }

    fun formatDistance(distance: Double): String {
        val roundedDistance = String.format("%.2f", distance)
        return "$roundedDistance km"
    }

    //  Exibe um snackbar com styles de error.
    fun showSnackBarError(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.holo_red_dark))
        val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }


    // Formata um objeto Timestamp para uma string de data e hora
    fun formatTimestamp(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }


    // Retorna as iniciais de um nome, separando-o por espaços.
    fun getInitials(name: String): String {
        val words = name.split(" ")
        val initials = StringBuilder()

        for (word in words) {
            initials.append(word[0])
        }

        return initials.toString()
    }


    // Calcula a média da nota, com base da lista de reviews.
    fun calculateAverageRating(ratings: List<Float>): Double {
        if (ratings.isEmpty()) {
            return 0.0
        }
        val totalRating = ratings.sum()
        return totalRating.toDouble() / ratings.size
    }

    // Realiza a geocodificação de um endereço usando a API do Google Maps
    fun geocodeAddress(address: String): Pair<Double, Double>? {
        val apiKey = "KEYMAP" // chave
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val geocodingResult: GeocodingResult?
        try {
            geocodingResult = GeocodingApi.geocode(geoApiContext, address).await().firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return if (geocodingResult != null) {
            val latitude = geocodingResult.geometry.location.lat
            val longitude = geocodingResult.geometry.location.lng
            Pair(latitude, longitude)
        } else {
            null
        }
    }

    /// Formata um objeto Timestamp para uma string de data ( dd / mm / yy)
    fun formatTimestampReviews(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }

    // Formata um valor de classificação (rating) para uma string com uma casa decimal.
    fun formatRating(rating: Double?): String {
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.maximumFractionDigits = 1
        return decimalFormat.format(rating)
    }

    // Traduz um status para outro idioma, com base em uma correspondência específica.
    fun translateStatus(status: String?): String {
        return when (status) {
            "drafting" -> "rascunho"
            "waiting" -> "aguardando"
            "onGoing" -> "em andamento"
            "reviewing" -> "em revisão"
            "finished" -> "finalizado"
            "canceled" -> "cancelado"
            "rejected" -> "rejeitado"
            "accepted" -> "aceito"
            else -> ""
        }
    }

    // Define a cor do ícone de status em um ImageView com base em um status específico
     fun setStatusIconColor(imageView: ImageView, status: String) {
        val colorResId = when (status) {
            "waiting" -> R.color.colorStatus2
            "onGoing" -> R.color.orange
            "finished" -> R.color.red
            else -> R.color.white
        }
        val color = ContextCompat.getColor(imageView.context, colorResId)
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    // Define um texto sublinhado em um TextView
    fun TextView.setUnderlinedText(text: String){
        val underlineSpan = UnderlineSpan()
        val spannableString = SpannableString(text)
        spannableString.setSpan(underlineSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        this.text = spannableString
    }

    // Define o estado de erro em um TextInputLayout, alterando as cores e os ícones.
    fun TextInputLayout.setErrorState() {
        val errorColor = Color.parseColor("#FF5252")
        val errorColorStateList = ColorStateList.valueOf(errorColor)
        val errorColorFilter = PorterDuffColorFilter(errorColor, PorterDuff.Mode.SRC_IN)

        defaultHintTextColor = errorColorStateList
        boxStrokeColor = errorColor
        startIconDrawable?.colorFilter = errorColorFilter
        endIconDrawable?.colorFilter = errorColorFilter
    }

    //Formata um objeto de  um endereço para uma string para usar no GeoApi.
    fun getFullAddress(street: String?, number: String?, neighborhood: String?, city: String?, state: String?, zipCode: String?): String {
        return "$street, $number, $neighborhood, $city, $state, $zipCode"
    }



    // Exibe um snackbar.
    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


    //Formata um objeto de  um endereço para uma string completa.
    fun formatAddress(address: Address): String {
        return "${address.street}, ${address.number}, ${address.neighborhood}, ${address.zipeCode}, ${address.city}, ${address.state}"
    }

    //Carrega uma imagem de uma URL usando a biblioteca Glide.
    fun loadImageFromUrl(url: String, view: CircleImageView) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }

    // Oculta o teclado.
    fun Fragment.hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    // Atualiza a imagem de perfil do usuário, fazendo upload de uma nova imagem para o Firebase Storage e retornando a URL da imagem atualizada.
    fun updateProfileImage(img: Bitmap?, userId: String): Task<Uri> {
        val baos = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageReference = FirebaseStorage.getInstance().getReference("USERS/PHOTOS/$userId")
        val oldPhotoUrlTask = storageReference.downloadUrl
        val deleteTask = oldPhotoUrlTask.continueWithTask { oldPhotoUrl ->
            FirebaseStorage.getInstance().getReferenceFromUrl(oldPhotoUrl.toString()).delete()
        }

        val uploadTask = deleteTask.continueWithTask {
            storageReference.putBytes(data)
        }
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageReference.downloadUrl
        }
    }


    // Faz o upload de uma  imagem de perfil do usuário para o Firebase Storage e retorna a URL da imagem.
    fun uploadProfileImage(img: Bitmap?, userId: String): Task<Uri> {
        val baos = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageReference = FirebaseStorage.getInstance().getReference("USERS/PHOTOS/$userId")
        val uploadTask = storageReference.putBytes(data)
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageReference.downloadUrl
        }
    }



    // Obtém uma mensagem de erro amigável com base em uma exceção do Firebase.
    fun getFirebaseErrorMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseNetworkException -> "Falha na conexão com a internet"
            is FirebaseAuthWeakPasswordException -> "Senha invalida!"
            is FirebaseAuthInvalidCredentialsException -> "Login ou senha estão inválidos"
            is FirebaseAuthUserCollisionException -> "Já existe uma conta com este e-mail."
            is FirebaseAuthEmailException -> "Endereço de e-mail inválido."
            is FirebaseAuthActionCodeException -> "O código de verificação é inválido ou expirou."
            is FirebaseAuthInvalidUserException -> "Usuário inválido."
            is FirebaseAuthRecentLoginRequiredException -> "É necessário fazer login novamente."
            is FirebaseAuthWebException -> "Erro ao processar solicitação. Tente novamente mais tarde."
            is FirebaseTooManyRequestsException -> "Você realizou muitas solicitações. Tente novamente mais tarde."
            is FirebaseException -> "Erro ao processar solicitação. Tente novamente mais tarde."
            else -> "Ocorreu um erro desconhecido. Tente novamente mais tarde."
        }
    }



}
