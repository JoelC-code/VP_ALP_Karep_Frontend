package com.example.vp_alp_karep_frontend.views.templates

import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vp_alp_karep_frontend.models.JobTagModel

@Composable
fun JobTagChecklist(
    tags: List<JobTagModel>
) {
    val checkedTags = remember { mutableStateMapOf<Int, Boolean>() }

    Column {
        tags.forEach { tag ->
            val checked = checkedTags[tag.id] == true

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checkedTags[tag.id] = it
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(text = tag.name)
            }
            HorizontalDivider(
                modifier = Modifier.padding(8.dp),
                thickness = 2.dp,
                color = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobTagChecklistPreview() {
    val fakeTags = listOf(
        JobTagModel(
            id = 1,
            name = "Android Developer"
        ),
        JobTagModel(
            id = 2,
            name = "Backend Engineer"
        )
    )

    JobTagChecklist(tags = fakeTags)
}

