package com.example.vp_alp_karep_frontend

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class AppContainer (
    private val dataStore: DataStore<Preferences>
) {
    /*IPaddress di cari dengan cara
        1. Buka cmd
        2. Ketik "ipconfig"
        3. Cari "IPv4 Address" pada bagian koneksi yang digunakan (Wi-Fi)
        4. Ganti "192.168.x.xx" dengan IPv4 Address yang ditemukan
     */
    private val backendURL = "http://192.168.x.xx:3000"
}