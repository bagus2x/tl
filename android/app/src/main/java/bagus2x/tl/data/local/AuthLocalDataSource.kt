package bagus2x.tl.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import bagus2x.tl.data.dto.AuthDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val AccessToken = stringPreferencesKey("access_token")
private val RefreshToken = stringPreferencesKey("refresh_token")
private val Verified = booleanPreferencesKey("verified")

class AuthLocalDataSource(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun save(auth: AuthDTO) {
        dataStore.edit { authPref ->
            authPref[AccessToken] = auth.accessToken
            authPref[RefreshToken] = auth.refreshToken
            authPref[Verified] = auth.verified
        }
    }

    fun getAuth(): Flow<AuthDTO?> {
        return dataStore.data.map { authPref ->
            AuthDTO(
                accessToken = authPref[AccessToken] ?: return@map null,
                refreshToken = authPref[RefreshToken] ?: return@map null,
                verified = authPref[Verified] ?: return@map null
            )
        }
    }

    suspend fun delete() {
        dataStore.edit { authPref ->
            authPref.remove(AccessToken)
            authPref.remove(RefreshToken)
            authPref.remove(Verified)
        }
    }
}
