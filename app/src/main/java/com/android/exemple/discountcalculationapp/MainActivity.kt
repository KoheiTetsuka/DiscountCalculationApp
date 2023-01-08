package com.android.exemple.discountcalculationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.android.exemple.discountcalculationapp.ui.theme.DiscountCalculationAppTheme
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscountCalculationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DiscountCalculationScreen()
                }
            }
        }
    }
}

@Composable
fun DiscountCalculationScreen() {
    var amountInput by remember { mutableStateOf("") }
    var discountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val discountPercent = discountInput.toDoubleOrNull() ?: 0.0
    val discount = calculateDiscount(amount, discountPercent)
    val focusManager = LocalFocusManager.current

    Column {
        TopAppBar(
            title = {
                Text(stringResource(R.string.DiscountCalculation))
            }
        )
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EditNumberField(
                label = R.string.last_price,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                value = amountInput,
                onValueChange = { amountInput = it }
            )
            EditNumberField(
                label = R.string.discount,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                value = discountInput,
                onValueChange = { discountInput = it }
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.discount_amount, discount),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@VisibleForTesting
internal fun calculateDiscount(
    amount: Double,
    discountPercent: Double
): String {
    var discountAmount = amount * (1 - discountPercent / 100)
    return NumberFormat.getCurrencyInstance(Locale.JAPAN).format(discountAmount)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DiscountCalculationAppTheme {
        DiscountCalculationScreen()
    }
}