package io.wexchain.android.dcc.view.adapters

import io.wexchain.android.dcc.repo.db.BeneficiaryAddress
import io.wexchain.android.dcc.view.adapter.DataBindAdapter
import io.wexchain.android.dcc.view.adapter.ItemViewClickListener
import io.wexchain.android.dcc.view.adapter.itemDiffCallback
import io.wexchain.dcc.R
import io.wexchain.dcc.databinding.ItemBeneficiaryAddressBinding

class BeneficiaryAddressAdapter(
        itemViewClickListener: ItemViewClickListener<BeneficiaryAddress>
) : DataBindAdapter<ItemBeneficiaryAddressBinding, BeneficiaryAddress>(
        layout = R.layout.item_beneficiary_address,
        itemDiffCallback = itemDiffCallback({ a, b -> a.address == b.address }),
        itemViewClickListener = itemViewClickListener
) {

    override fun bindData(binding: ItemBeneficiaryAddressBinding, item: BeneficiaryAddress?) {
        binding.addr = item
    }
}
