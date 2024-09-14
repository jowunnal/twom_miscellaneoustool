package com.jinproject.features.simulator.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.design_compose.component.DescriptionMediumText
import com.jinproject.design_compose.component.DescriptionSmallText
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.simulator.EquipmentListPreviewParameters
import com.jinproject.features.simulator.model.Armor
import com.jinproject.features.simulator.model.Equipment
import com.jinproject.features.simulator.model.Weapon
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ItemDetail(
    context: Context = LocalContext.current,
    equipment: Equipment,
) {
    Column(
        Modifier
            .wrapContentWidth()
            .shadow(20.dp, RoundedCornerShape(10.dp))
            .background(
                MiscellaneousToolColor.itemDetailContentColor.color, RoundedCornerShape(6.dp)
            )
            .padding(8.dp)
    ) {
        DescriptionMediumText(
            text = equipment.name,
            color = MiscellaneousToolColor.itemNameColor.color,
        )
        HorizontalDivider(
            modifier = Modifier
                .width(100.dp)
                .padding(horizontal = 2.dp, vertical = 8.dp),
            color = MiscellaneousToolColor.itemButtonColor.color,
        )
        DescriptionSmallText(
            text = "요구 레벨 ${equipment.level}",
            color = MiscellaneousToolColor.itemNameColor.color,
        )
        VerticalSpacer(height = 4.dp)

        val otherItemDescription = when (equipment) {
            is Weapon -> {
                val enchantedDamage = equipment.getDamageRangeEnchanted(context)

                val damageText = "공격 ${enchantedDamage.first}-${enchantedDamage.last}"
                val speedText = "속도 ${equipment.speed}"
                val optionsText = equipment.options.filter { option ->
                    option.value.toInt() != 0
                }.joinToString("\n") { option ->
                    "${
                        context.doOnLocaleLanguage(
                            onKo = option.name.displayName,
                            onElse = option.name.displayOtherLanguage
                        )
                    } ${option.value.toInt()}"
                }

                "$damageText\n$speedText\n$optionsText"
            }

            is Armor -> {
                val armorText = "방어력 ${equipment.getArmorEnchanted()}"
                val optionsText = equipment.options.joinToString("\n") { "${it.name} ${it.value}" }

                "$armorText\n$optionsText"
            }

            else -> {
                throw IllegalStateException("$equipment is not allowed")
            }
        }

        DescriptionSmallText(
            text = otherItemDescription, color = MiscellaneousToolColor.itemDescriptionColor.color
        )

    }
}

@Preview(widthDp = 180, heightDp = 180)
@Composable
private fun PreviewItemDetail(
    @PreviewParameter(EquipmentListPreviewParameters::class)
    equipments: ImmutableList<Equipment>,
) = MiscellaneousToolTheme {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        ItemDetail(equipment = equipments.last())
    }
}