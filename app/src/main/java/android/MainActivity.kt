@file:Suppress("DEPRECATION")

package android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONArray

class MainActivity : ComponentActivity() {

    private var currentBarcode by mutableStateOf("")
    private var productData by mutableStateOf<ProductData?>(null)

    private val barcodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            if (intentResult != null && intentResult.contents != null) {
                currentBarcode = intentResult.contents
                Toast.makeText(this, "📦 Barkod: $currentBarcode", Toast.LENGTH_SHORT).show()
                getProductInfo(currentBarcode)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        ManualBarcodeEntry { barcode ->
                            currentBarcode = barcode
                            getProductInfo(barcode)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        BarcodeScanButton {
                            val integrator = IntentIntegrator(activity)
                            integrator.setOrientationLocked(false)
                            integrator.setBeepEnabled(true)
                            integrator.setPrompt("Lütfen barkodu taratın")
                            barcodeLauncher.launch(integrator.createScanIntent())
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        productData?.let { product ->
                            UpdateForm(
                                barcode = currentBarcode,
                                name = product.name,
                                price = product.price.toString(),
                                stock = product.stock.toString(),
                                category = product.category
                            ) { barcode, name, price, stock, category ->
                                updateProductInfo(barcode, name, price, stock, category)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getProductInfo(barcode: String) {
        val url = "http://10.0.2.2/barkod_app/get_product.php?barcode=$barcode"




        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response: JSONArray ->
                if (response.length() > 0) {
                    val product = response.getJSONObject(0)
                    productData = ProductData(
                        product.getString("name"),
                        product.getDouble("price"),
                        product.getInt("stock"),
                        product.getString("category")
                    )
                } else {
                    Toast.makeText(this, "🚫 Ürün bulunamadı!", Toast.LENGTH_SHORT).show()
                    productData = null
                }
            },
            { error ->
                Toast.makeText(this, "❌ Hata: ${error.message}", Toast.LENGTH_SHORT).show()
                productData = null
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun updateProductInfo(barcode: String, name: String, price: Double, stock: Int, category: String) {
        val url = "http://10.0.2.2/barkod_app/update_product.php"


        val params = HashMap<String, String>().apply {
            put("barcode", barcode)
            put("name", name)
            put("price", price.toString())
            put("stock", stock.toString())
            put("category", category)
        }

        val request = object : StringRequest(Method.POST, url,
            { response ->
                Toast.makeText(this, "✅ Güncelleme başarılı!", Toast.LENGTH_SHORT).show()
                productData = null // 👈 Formu kapat
            },
            { error ->
                Toast.makeText(this, "❌ Güncelleme hatası: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> = params
        }

        Volley.newRequestQueue(this).add(request)
    }

    @Composable
    fun ManualBarcodeEntry(onSubmit: (String) -> Unit) {
        var barcodeText by remember { mutableStateOf("") }

        Column {
            OutlinedTextField(
                value = barcodeText,
                onValueChange = { barcodeText = it },
                label = { Text("Barkod gir") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = {
                    if (barcodeText.isBlank()) {
                        Toast.makeText(this@MainActivity, "⚠️ Barkod boş olamaz", Toast.LENGTH_SHORT).show()
                    } else {
                        onSubmit(barcodeText)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ürünü Getir")
            }
        }
    }

    @Composable
    fun BarcodeScanButton(onScanClick: () -> Unit) {
        Button(
            onClick = onScanClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📷 Barkod Tara")
        }
    }

    @Composable
    fun UpdateForm(
        barcode: String,
        name: String,
        price: String,
        stock: String,
        category: String,
        onUpdateClick: (String, String, Double, Int, String) -> Unit
    ) {
        var nameState by remember { mutableStateOf(name) }
        var priceState by remember { mutableStateOf(price) }
        var stockState by remember { mutableStateOf(stock) }
        var categoryState by remember { mutableStateOf(category) }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            OutlinedTextField(value = nameState, onValueChange = { nameState = it }, label = { Text("Ad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = priceState, onValueChange = { priceState = it }, label = { Text("Fiyat") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = stockState, onValueChange = { stockState = it }, label = { Text("Stok") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = categoryState, onValueChange = { categoryState = it }, label = { Text("Kategori") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nameState.isBlank() || priceState.isBlank() || stockState.isBlank() || categoryState.isBlank()) {
                        Toast.makeText(this@MainActivity, "⚠️ Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    onUpdateClick(
                        barcode,
                        nameState,
                        priceState.toDoubleOrNull() ?: 0.0,
                        stockState.toIntOrNull() ?: 0,
                        categoryState
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💾 Güncelle")
            }
        }
    }

    data class ProductData(
        val name: String,
        val price: Double,
        val stock: Int,
        val category: String
    )
}
