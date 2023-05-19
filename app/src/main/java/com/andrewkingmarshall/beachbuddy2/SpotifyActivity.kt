package com.andrewkingmarshall.beachbuddy2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.andrewkingmarshall.beachbuddy2.extensions.toast
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import timber.log.Timber


class SpotifyActivity : AppCompatActivity() {

    private val clientId = BuildConfig.SPOTIFY_API_KEY
    private val redirectUri = "beachbuddy2://spotify"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    // Set the connection parameters
    private val connectionParams by lazy {
        ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(false)
            .build()
    }

    private val REQUEST_CODE = 1337


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        findViewById<Button>(R.id.authButton).setOnClickListener {
            authFow()
        }

        findViewById<Button>(R.id.playButton).setOnClickListener {
            playFlow()
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            pauseFlow()
        }

        findViewById<Button>(R.id.getPlayListsButton).setOnClickListener {
            getPlaylists()
        }

    }

    private fun authFow() {

        val builder =
            AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectUri)

        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)

    }

    private fun playFlow() {
        "Play".toast(this)

        SpotifyAppRemote.disconnect(spotifyAppRemote)
        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Timber.tag("MainActivity").d("Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Timber.e(throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    var paused = false
    private fun pauseFlow() {
        paused = if (!paused) {
            spotifyAppRemote?.playerApi?.pause()
            true
        } else {
            spotifyAppRemote?.playerApi?.resume()
            false
        }

    }


    private fun getPlaylists() {
//        spotifyAppRemote?.contentApi?.
    }

    override fun onStart() {
        super.onStart()

    }

    private fun connected() {
        // Play a playlist
        spotifyAppRemote?.playerApi?.play("spotify:playlist:58u57zxO7K9PLcmTqanVQi")

        // Subscribe to PlayerState
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            val track: Track = it.track
            Timber.tag("MainActivity").d("${track.name} by ${track.artist.name}")
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Timber.d("It Worked!")
                    Timber.d("${response.accessToken} expires in ${response.expiresIn}")

                }
                AuthorizationResponse.Type.ERROR -> {
                    Timber.w("It did not work: ")

                }
                else -> {}
            }
        }
    }
}