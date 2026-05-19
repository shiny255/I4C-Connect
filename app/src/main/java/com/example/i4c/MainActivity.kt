package com.example.i4c

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.TextStyle
import com.example.i4c.PhoneScanEntity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Article
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding


data class SuspiciousApp(
    val appName: String,
    val packageName: String,
    val reason: String
)



const val PHONE_API_KEY = "C9X3UcZpqVne8W20OvMrNuif9WNnFflA"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CyberPremiumApp() }
    }
}

@Composable
fun CyberPremiumApp() {
    var showSplash by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(1800)
        showSplash = false
    }

    Box(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(),
            exit = fadeOut()
        ) { SplashScreen() }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn()
        ) { MainScreen() }
    }
}

@Composable
fun SplashScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3F2FD), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.cyber_logo),
                contentDescription = "App logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Indian Cyber Crime Coordination Centre",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
        }
    }
}
@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf("home") }

    BackHandler(enabled = selectedScreen != "home") {
        selectedScreen = "home"
    }
    // <-- fix: call LocalContext.current inside composable scope, once
    val context = LocalContext.current

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }


    // navigation
    when (selectedScreen) {
        "qr" -> {
            QrScannerScreen { selectedScreen = "home" }
            return
        }

        "help" -> {
            HelplineScreen { selectedScreen = "home" }
            return
        }
        "url" -> {
            UrlDetectorScreen { selectedScreen = "home" }
            return
        }
        "file" -> {
            FileScanScreen { selectedScreen = "home" }
            return
        }
        "mobile" -> {
            MobileScanScreen { selectedScreen = "home" }
            return
        }

        "device" -> {
            DeviceScanScreen { selectedScreen = "home" }
            return
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF0B1220))
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Quick Actions",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.height(500.dp)
            ) {

                item {
                    ActionCard(
                        title = "URL Scan",
                        subtitle = "Check website safety",
                        circleColor = Color(0xFF6366F1),
                        icon = Icons.Default.Link
                    ) {
                        selectedScreen = "url"
                    }
                }

                item {
                    ActionCard(
                        title = "File Scan",
                        subtitle = "Detect malware",
                        circleColor = Color(0xFF14B8A6),
                        icon = Icons.Default.InsertDriveFile
                    ) {
                        selectedScreen = "file"
                    }
                }

                item {
                    ActionCard(
                        title = "QR Code",
                        subtitle = "Verify QR safety",
                        circleColor = Color(0xFFFB923C),
                        icon = Icons.Default.QrCode
                    ) {
                        selectedScreen = "qr"
                    }
                }

                item {
                    ActionCard(
                        title = "WiFi Scan",
                        subtitle = "Network settings",
                        circleColor = Color(0xFF10B981),
                        icon = Icons.Default.Wifi
                    ) {
                        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                        context.startActivity(intent)
                    }
                }
                item {
                    ActionCard(
                        title = "Mobile Scan",
                        subtitle = "Check phone number risk",
                        circleColor = Color(0xFFEC4899),
                        icon = Icons.Default.Call
                    ) {
                        selectedScreen = "mobile"
                    }
                }

                item {
                    ActionCard(
                        title = "Device Scan",
                        subtitle = "Check device security",
                        circleColor = Color(0xFF22D3EE),
                        icon = Icons.Default.Article
                    ) {
                        selectedScreen = "device"
                    }
                }

                item {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:1930")
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) {
                        Text("Call 1930", fontSize = 14.sp)
                    }
                }

                item {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://cybercrime.gov.in")
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                    ) {
                        Text("Website", fontSize = 14.sp)
                    }
                }

            }


            // 2x2 grid of action cards

            Spacer(modifier = Modifier.height(20.dp))
            PremiumSecurityStatus(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    circleColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120)
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(circleColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                color = Color(0xFF94A3B8),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun PremiumSecurityStatus(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .navigationBarsPadding(), // ✅ This ensures the card is fully visible above navigation bar
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2A44)),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Security Status",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Your device is secure",
                color = Color(0xFF22C55E),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun UrlDetectorScreen(onBack: () -> Unit) {

    var url by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var hasResult by remember { mutableStateOf(false) }

    var malicious by remember { mutableStateOf(0) }
    var suspicious by remember { mutableStateOf(0) }
    var harmless by remember { mutableStateOf(0) }

    var registrar by remember { mutableStateOf("Unknown") }
    var country by remember { mutableStateOf("Unknown") }
    var creationDate by remember { mutableStateOf("Unknown") }
    var expiryDate by remember { mutableStateOf("Unknown") }

    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun formatDate(timestamp: Any?): String {
        return try {
            val time = timestamp.toString().toDouble().toLong() * 1000
            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            sdf.format(java.util.Date(time))
        } catch (e: Exception) {
            "Unknown"
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220))
            .statusBarsPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            "🔐 URL Threat Scanner",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            placeholder = { Text("Enter website URL") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Cyan
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                if (url.isEmpty()) {
                    errorMessage = "Please enter URL"
                    return@Button
                }

                isLoading = true
                hasResult = false
                errorMessage = ""

                scope.launch {
                    try {

                        var finalUrl = url.trim()
                        if (!finalUrl.startsWith("http"))
                            finalUrl = "https://$finalUrl"

                        // -------- VirusTotal --------
                        // -------- VirusTotal --------
                        val scan = RetrofitClient.virusApi
                            .scanUrl(VIRUS_API_KEY, finalUrl)

                        val id = scan.body()?.data?.id ?: ""

                        var status = "queued"
                        var maliciousTemp = 0
                        var suspiciousTemp = 0
                        var harmlessTemp = 0
                        var attempt = 0

                        while (status != "completed" && attempt < 6) {

                            delay(5000)
                            attempt++

                            val response = RetrofitClient.virusApi
                                .getAnalysis(VIRUS_API_KEY, id)

                            val body = response.body()

                            status = body?.data?.attributes?.status ?: "queued"

                            maliciousTemp = body?.data?.attributes
                                ?.lastAnalysisStats?.malicious ?: 0

                            suspiciousTemp = body?.data?.attributes
                                ?.lastAnalysisStats?.suspicious ?: 0

                            harmlessTemp = body?.data?.attributes
                                ?.lastAnalysisStats?.harmless ?: 0
                        }

                        malicious = maliciousTemp
                        suspicious = suspiciousTemp
                        harmless = harmlessTemp
                        // -------- API Ninja --------
                        val domain = Uri.parse(finalUrl).host ?: ""

                        val ninjaResponse =
                            RetrofitClient.ninjaApi.getWhois(
                                NINJA_API_KEY,
                                domain
                            )

                        val ninjaData = ninjaResponse.body()

                        registrar = ninjaData?.registrar ?: "Unknown"
                        country = ninjaData?.country ?: "Unknown"
                        creationDate = formatDate(ninjaData?.creation_date)
                        expiryDate = formatDate(ninjaData?.expiration_date)
                        hasResult = true

                    } catch (e: Exception) {
                        errorMessage = "Scan Failed"
                    }

                    isLoading = false
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan Now")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color.Cyan)
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
        }

        if (hasResult) {

            val total = malicious + suspicious + harmless
            val riskPercent =
                if (total > 0)
                    ((malicious + suspicious) * 100) / total
                else 0

            val grade = when {
                riskPercent == 0 -> "A"
                riskPercent < 20 -> "B"
                riskPercent < 50 -> "C"
                else -> "D"
            }

            Spacer(modifier = Modifier.height(20.dp))

            val riskColor = when {
                riskPercent == 0 -> Color(0xFF00E676)
                riskPercent < 30 -> Color(0xFFFFC107)
                else -> Color(0xFFFF5252)
            }

            Text(
                "Risk Level: $riskPercent%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = riskColor
            )

            LinearProgressIndicator(
                progress = riskPercent / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = riskColor,
                trackColor = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Grade: $grade", color = Color.Cyan)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111827)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "🌍 DOMAIN INTELLIGENCE",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Registrar: $registrar", color = Color.White)
                    Text("Country: $country", color = Color.White)
                    Text("Created: $creationDate", color = Color.White)
                    Text("Expires: $expiryDate", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
@Composable
fun FileScanScreen(onBack: () -> Unit) {
    var message by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            scope.launch {
                try {
                    message = "Calculating SHA-256..."
                    val input = context.contentResolver.openInputStream(uri)
                    val bytes = input?.readBytes()
                    val hash = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
                    message = "Checking VirusTotal..."
                    val response = RetrofitClient.virusApi.getFileReport(VIRUS_API_KEY, hash)
                    val stats = response.body()?.data?.attributes?.lastAnalysisStats
                    val malicious = stats?.malicious ?: 0
                    result = if (malicious > 0) "🔴 File is Malicious" else "🟢 File is Safe"
                    message = "Scan Completed"
                } catch (e: Exception) {
                    message = "Error"
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize() .statusBarsPadding() .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("File Security Scan", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { launcher.launch("*/*") }) { Text("Select File") }
        Spacer(modifier = Modifier.height(20.dp))
        Text(message)
        Spacer(modifier = Modifier.height(10.dp))
        Text(result)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun HelplineScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Cyber Crime Helpline", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(30.dp))
        Text("Helpline Number: 1930", color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:1930") }
            context.startActivity(intent)
        }) { Text("Call 1930") }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://cybercrime.gov.in/Webform/crmcondi.aspx") }
            context.startActivity(intent)
        }) { Text("Open Website") }

        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onBack) { Text("Back") }
    }
}

// helper for formatting unix-like timestamps if needed
fun formatUnixTime(timestamp: Long?): String {
    if (timestamp == null) return "Unknown"
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("dd-MM-yyyy")
    return format.format(date)
}

@Composable
fun QrScannerScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var scannedText by remember { mutableStateOf("") }
    var malwarePercent by remember { mutableStateOf(0) }
    var suspiciousPercent by remember { mutableStateOf(0) }
    var safePercent by remember { mutableStateOf(0) }
    var isChecking by remember { mutableStateOf(false) }
    var hasResult by remember { mutableStateOf(false) }

    val cameraProviderFuture =
        androidx.camera.lifecycle.ProcessCameraProvider.getInstance(context)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->

            val previewView = androidx.camera.view.PreviewView(ctx)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                val imageAnalyzer =
                    androidx.camera.core.ImageAnalysis.Builder()
                        .setBackpressureStrategy(
                            androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                        )
                        .build()

                val barcodeScanner =
                    com.google.mlkit.vision.barcode.BarcodeScanning.getClient()

                imageAnalyzer.setAnalyzer(
                    java.util.concurrent.Executors.newSingleThreadExecutor()
                ) { imageProxy ->

                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {

                        val image =
                            com.google.mlkit.vision.common.InputImage
                                .fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {

                                    val value = barcode.rawValue ?: ""

                                    if (value.startsWith("http") && !isChecking) {

                                        scannedText = value
                                        isChecking = true
                                        hasResult = false

                                        scope.launch {

                                            try {

                                                var baseUrl = value
                                                    .replace("https://", "")
                                                    .replace("http://", "")

                                                val httpsUrl = "https://$baseUrl"
                                                val httpUrl = "http://$baseUrl"

                                                suspend fun scan(url: String): AnalysisStats? {

                                                    val urlId = android.util.Base64.encodeToString(
                                                        url.toByteArray(),
                                                        android.util.Base64.NO_WRAP
                                                    ).trimEnd('=')

                                                    val response = RetrofitClient.virusApi
                                                        .getUrlReport(VIRUS_API_KEY, urlId)

                                                    return response.body()
                                                        ?.data
                                                        ?.attributes
                                                        ?.lastAnalysisStats
                                                }



                                                val httpsStats = scan(httpsUrl)
                                                val httpStats = scan(httpUrl)

                                                val stats = httpsStats ?: httpStats

                                                if (stats != null) {

                                                    val total =
                                                        stats.malicious +
                                                                stats.suspicious +
                                                                stats.harmless +
                                                                stats.undetected

                                                    if (total > 0) {
                                                        malwarePercent =
                                                            (stats.malicious * 100) / total
                                                        suspiciousPercent =
                                                            (stats.suspicious * 100) / total
                                                        safePercent =
                                                            (stats.harmless * 100) / total
                                                    }

                                                    hasResult = true
                                                }

                                            } catch (e: Exception) {
                                                hasResult = false
                                            }

                                            isChecking = false
                                        }
                                    }
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )

            }, context.mainExecutor)

            previewView
        }
    )

    // ---------------- PROFESSIONAL RESULT UI ----------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF111827)
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (scannedText.isEmpty())
                        "Scan a QR Code"
                    else scannedText,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isChecking) {
                    CircularProgressIndicator(color = Color.Cyan)
                }

                if (hasResult) {

                    RiskBar("Malware", malwarePercent, Color.Red)
                    RiskBar("Suspicious", suspiciousPercent, Color(0xFFFF9800))
                    RiskBar("Safe", safePercent, Color.Green)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = onBack) {
                    Text("Back")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}


@Composable
fun RiskBar(label: String, percent: Int, color: Color) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color.White)
            Text("$percent%", color = color, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = percent / 100f,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            trackColor = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun MobileScanScreen(onBack: () -> Unit) {

    val scope = rememberCoroutineScope()

    var phone by remember { mutableStateOf("") }

    var fraudScore by remember { mutableStateOf<Int?>(null) }
    var riskLevel by remember { mutableStateOf("") }
    var carrier by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var reportedAbuse by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220))
            .statusBarsPadding()
            .padding(20.dp)
    ) {

        // Header
        Text(
            text = "Mobile Number Scanner",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Phone Input Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF111827)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "Enter Phone Number",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        "+",
                        color = Color.White,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = {
                            Text(
                                "Country Code + Number (e.g. 919876543210)",
                                color = Color.Gray
                            )
                        },
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Cyan,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Cyan
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (phone.isEmpty()) {
                    errorMessage = "Please enter a valid number with country code."
                    return@Button
                }

                errorMessage = ""
                fraudScore = null
                isLoading = true

                scope.launch {
                    try {
                        val response = RetrofitClient.phoneApi
                            .checkPhone(Constants.PHONE_API_KEY, phone)

                        val body = response.body()

                        if (body != null && body.success) {

                            fraudScore = body.fraudScore

                            riskLevel = when {
                                body.fraudScore > 80 || body.recentAbuse || body.spam ->
                                    "High Risk"

                                body.fraudScore > 50 || body.voip ->
                                    "Suspicious"

                                else ->
                                    "Low Risk"
                            }

                            carrier = body.carrier ?: "Unknown"
                            country = body.country ?: "Unknown"
                            reportedAbuse = body.recentAbuse

                        } else {
                            errorMessage = "No data found."
                        }

                    } catch (e: Exception) {
                        errorMessage = "Error checking number."
                    }

                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Scan Now")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Cyan)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        fraudScore?.let {

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111827)
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    // Risk Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                when (riskLevel) {
                                    "High Risk" -> Color(0xFF7F1D1D)
                                    "Suspicious" -> Color(0xFF78350F)
                                    else -> Color(0xFF064E3B)
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = riskLevel,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FraudMeter(it)
                    Spacer(modifier = Modifier.height(20.dp))

                    InfoRow("Carrier", carrier)
                    InfoRow("Country", country)
                    InfoRow(
                        "Reported Abuse",
                        if (reportedAbuse) "Yes" else "No"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
@Composable
fun DeviceScanScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val packageManager = context.packageManager

    var installedApps by remember { mutableStateOf(0) }
    var suspiciousApps by remember { mutableStateOf(0) }
    var systemApps by remember { mutableStateOf(0) }
    var userApps by remember { mutableStateOf(0) }

    var suspiciousAppList by remember { mutableStateOf<List<SuspiciousApp>>(emptyList()) }

    val androidVersion = android.os.Build.VERSION.RELEASE
    val securityPatch = android.os.Build.VERSION.SECURITY_PATCH
    val model = android.os.Build.MODEL

    val isEmulator =
        android.os.Build.FINGERPRINT.contains("generic") ||
                android.os.Build.MODEL.contains("Emulator")

    val isUsbDebuggingEnabled =
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) == 1

    val developerOptionsEnabled =
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) == 1

    val isRooted = checkRoot()

    LaunchedEffect(Unit) {

        val packages = packageManager.getInstalledApplications(0)

        installedApps = packages.size
        suspiciousApps = 0
        systemApps = 0
        userApps = 0

        val tempSuspiciousList = mutableListOf<SuspiciousApp>()

        packages.forEach { app ->

            val isSystemApp =
                (app.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0

            if (isSystemApp) {
                systemApps++
                return@forEach
            }

            userApps++

            val installer =
                packageManager.getInstallerPackageName(app.packageName)

            val installedFromPlayStore =
                installer == "com.android.vending"

            var reason: String? = null

            if (!installedFromPlayStore) {
                suspiciousApps++
                reason = "Installed outside Play Store"
            }

            try {
                val packageInfo = packageManager.getPackageInfo(
                    app.packageName,
                    PackageManager.GET_PERMISSIONS
                )

                val permissions =
                    packageInfo.requestedPermissions ?: emptyArray()

                val dangerousPermissions = listOf(
                    "android.permission.READ_SMS",
                    "android.permission.SEND_SMS",
                    "android.permission.RECEIVE_SMS",
                    "android.permission.READ_CONTACTS",
                    "android.permission.SYSTEM_ALERT_WINDOW",
                    "android.permission.BIND_ACCESSIBILITY_SERVICE"
                )

                val hasDangerousPermission =
                    permissions.any { dangerousPermissions.contains(it) }

                if (hasDangerousPermission) {
                    suspiciousApps++
                    reason = if (reason != null)
                        "$reason + Dangerous permissions"
                    else
                        "Uses dangerous permissions"
                }

            } catch (_: Exception) {}

            if (reason != null) {
                val appName = packageManager.getApplicationLabel(app).toString()

                tempSuspiciousList.add(
                    SuspiciousApp(
                        appName = appName,
                        packageName = app.packageName,
                        reason = reason
                    )
                )
            }
        }

        suspiciousAppList = tempSuspiciousList
    }

    val riskLevel = when {
        isRooted -> "🔴 Rooted Device"
        suspiciousApps > 3 -> "🟡 Suspicious Apps Found"
        isUsbDebuggingEnabled -> "🟡 USB Debugging Enabled"
        developerOptionsEnabled -> "🟡 Developer Mode Enabled"
        else -> "🟢 Secure Device"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            "Advanced Device Scan",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        DeviceInfoCard("Model", model)
        DeviceInfoCard("Android Version", androidVersion)
        DeviceInfoCard("Security Patch", securityPatch)

        Spacer(modifier = Modifier.height(16.dp))

        DeviceInfoCard("Installed Apps", installedApps.toString())
        DeviceInfoCard("User Apps", userApps.toString())
        DeviceInfoCard("System Apps", systemApps.toString())
        DeviceInfoCard("Suspicious Apps", suspiciousApps.toString())

        Spacer(modifier = Modifier.height(16.dp))

        DeviceInfoCard("Emulator", if (isEmulator) "Yes" else "No")
        DeviceInfoCard("USB Debugging", if (isUsbDebuggingEnabled) "Enabled" else "Disabled")
        DeviceInfoCard("Developer Mode", if (developerOptionsEnabled) "Enabled" else "Disabled")
        DeviceInfoCard("Root Status", if (isRooted) "Rooted" else "Not Rooted")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = riskLevel,
            color = when {
                riskLevel.contains("🔴") -> Color.Red
                riskLevel.contains("🟡") -> Color.Yellow
                else -> Color.Green
            },
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Suspicious App List",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (suspiciousAppList.isEmpty()) {
            Text(
                "No suspicious apps found",
                color = Color.Green
            )
        } else {
            suspiciousAppList.forEach { app ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(app.appName, fontWeight = FontWeight.Bold)
                        Text(app.packageName, fontSize = 12.sp)

                        Text(
                            app.reason,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
@Composable
fun DeviceInfoCard(title: String, value: String) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(title, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color.White)
        }
    }
}
@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.Gray)
        Text(value, color = Color.White, fontWeight = FontWeight.Medium)
    }
}
@Composable
fun FraudMeter(score: Int) {

    val animatedProgress by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(1200)
    )

    val color = when {
        score > 80 -> Color.Red
        score > 50 -> Color(0xFFFFA500)
        else -> Color(0xFF22C55E)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(180.dp)
    ) {

        CircularProgressIndicator(
            progress = animatedProgress,
            strokeWidth = 14.dp,
            color = color,
            modifier = Modifier.fillMaxSize()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$score%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Fraud Risk",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
@Composable
fun PhoneHistoryScreen(
    history: List<PhoneScanEntity>,
    onDelete: (PhoneScanEntity) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220))
            .padding(16.dp)
    ) {

        Text(
            "Scan History",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(history) { item ->

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111827)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(item.phone, color = Color.White)
                            Text(
                                "Fraud: ${item.fraudScore}%",
                                color = Color.Gray
                            )
                            Text(
                                item.riskLevel,
                                color = when {
                                    item.fraudScore > 80 -> Color.Red
                                    item.fraudScore > 50 -> Color.Yellow
                                    else -> Color.Green
                                }
                            )
                        }

                        IconButton(
                            onClick = { onDelete(item) }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
@Composable
fun DashboardScreen(
    totalScans: Int,
    highRisk: Int,
    suspicious: Int,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1220))
            .padding(20.dp)
    ) {

        Text(
            "Security Dashboard",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        DashboardCard("Total Scans", totalScans.toString(), Color.Cyan)
        DashboardCard("High Risk Numbers", highRisk.toString(), Color.Red)
        DashboardCard("Suspicious Numbers", suspicious.toString(), Color.Yellow)

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, color: Color) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.Gray)
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

fun checkRoot(): Boolean {
    val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su"
    )

    return paths.any { java.io.File(it).exists() }
}

