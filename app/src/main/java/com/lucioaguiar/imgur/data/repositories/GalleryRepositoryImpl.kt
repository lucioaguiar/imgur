package com.lucioaguiar.imgur.data.repositories

import android.os.RemoteException
import com.lucioaguiar.imgur.data.models.Image
import com.lucioaguiar.imgur.util.UiState
import com.lucioaguiar.imgur.rest.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryRepositoryImpl(
    private val retrofitService: RetrofitService
) : GalleryRepository {

    override suspend fun getData(page: Int, query: String, type: String, size: String): UiState<List<Image>> {
        return withContext(Dispatchers.IO) {
            try {
                val data = retrofitService.galleries(page, query, type, size)
                UiState.Success(data.getAllImages())
            } catch (exception: RemoteException) {
                UiState.Failure("Você está offline, conecte-se antes de continuar.")
            } catch (exception: Exception) {
                UiState.Failure("Ocorreu alguma tipo de erro, tente novamente")
            }
        }
    }

}