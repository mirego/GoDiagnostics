// Copyright (c) 2017, Mirego
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// - Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// - Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// - Neither the name of the Mirego nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
package com.mirego.godiagnostics.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirego.godiagnostics.DiagnosticInfoViewData;
import com.mirego.godiagnostics.R;

import java.util.List;


public class DiagnosticsInfoAdapter extends RecyclerView.Adapter<DiagnosticsInfoViewHolder> {

    private List<DiagnosticInfoViewData> diagnosticInfoList;

    public DiagnosticsInfoAdapter() {
    }

    @Override
    public DiagnosticsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diagnostics_info, parent, false);

        return new DiagnosticsInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiagnosticsInfoViewHolder holder, int position) {
        holder.setDiagnosticInfoViewData(diagnosticInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        if (diagnosticInfoList == null) {
            return 0;
        }
        return diagnosticInfoList.size();
    }

    public void setData(List<DiagnosticInfoViewData> diagnosticInfoList) {
        this.diagnosticInfoList = diagnosticInfoList;
        notifyDataSetChanged();
    }

}

class DiagnosticsInfoViewHolder extends RecyclerView.ViewHolder {

    DiagnosticsInfoViewHolder(View itemView) {
        super(itemView);
    }

    void setDiagnosticInfoViewData(DiagnosticInfoViewData viewData) {
        TextView itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        itemTitle.setText(viewData.getTitle());

        TextView itemValue = (TextView) itemView.findViewById(R.id.item_value);
        itemValue.setText(viewData.getValue());
    }
}
