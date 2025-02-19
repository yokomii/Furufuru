package dev.iaiabot.furufuru.feature.ui.issue

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FurufuruTextField(
    text: String,
    label: String,
    multiline: Boolean = false,
    onChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = !multiline,
        maxLines = if (multiline) 4 else 1,
    )
}

@Composable
fun SendButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    if (!isVisible) {
        FloatingActionButton(onClick = onClick) {
            Icon(Icons.Filled.Send, contentDescription = "send")
        }
    }
}

@Composable
fun Progress(isVisible: Boolean) {
    if (isVisible) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xa1111111),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ImageContent(
    fileStr: String?
) {
    if (fileStr.isNullOrBlank()) {
        return
    }
    val decodedString: ByteArray = Base64.decode(fileStr.trim(), Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun IssueLabels() {
    // TODO: 未対応 ref: https://material.io/blog/jetpack-compose-beta
    /*
        model.labels.observe(this) { labels ->
            val chips = labels.map { label ->
                Chip(this).apply {
                    text = label
                    isCheckable = true
                    setOnCheckedChangeListener { _, isChecked ->
                        model.onCheckedChangeLabel(isChecked, label)
                    }
                }
            }
        }
     */
}

@Composable
@Preview
fun FurufuruTextFieldPreview() {
    FurufuruTextField(
        label = "furufuru text",
        text = "foo bar",
        onChange = { },
    )
}

@Composable
@Preview
fun FurufuruTextFieldMultilinePreview() {
    FurufuruTextField(
        label = "furufuru text",
        text = "foo bar",
        multiline = true,
        onChange = { }
    )
}

@Composable
@Preview
fun SendButtonPreview() {
    SendButton(isVisible = false, onClick = { })
}

@Composable
@Preview
fun ProgressPreview() {
    Progress(isVisible = true)
}

@Composable
@Preview
fun ImageContentPreview() {
    ImageContent(
        fileStr = """
iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=
"""
    )
}
