//
//  ProgramEditorView.swift
//  iosApp
//
//  Created by Satoshi Kobayashi on 2020/11/23.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProgramEditorView: View {
    @ObservedObject
    var viewModel: ProgramEditorViewModel
    
    private let onDismiss: (() -> Void)?
    
    init(viewModel: ProgramEditorViewModel, onDismiss: (() -> Void)? = nil) {
        self.viewModel = viewModel
        self.onDismiss = onDismiss
    }
    
    var body: some View {
        VStack {
            TextField("title", text: $viewModel.title)
                .padding()
            ZStack {
                RoundedRectangle(cornerRadius: 4)
                    .strokeBorder()
                    .foregroundColor(.gray)
                TextEditor(text: $viewModel.input)
                    .disableAutocorrection(true)
                    .padding()
            }
            .frame(minHeight: 50, maxHeight: 200)
            .padding()
            Button(action: {
                viewModel.runProgram()
            }, label: {
                Text("Run")
            })
            .padding()
            switch viewModel.output {
            case .success(let output):
                Text(output)
                    .padding()
            case .error(let message):
                Text(message)
                    .foregroundColor(.red)
                    .padding()
            }
            Spacer()
        }
        .navigationBarTitle("Editor", displayMode: .large)
        .navigationBarItems(trailing: HStack {
            Button(action: {
                viewModel.saveProgram()
            }, label: {
                Text("Save")
            })
        })
        .onDisappear {
            onDismiss?()
        }
    }
}

struct ProgramEditorView_Previews: PreviewProvider {
    static var previews: some View {
        ProgramEditorView(viewModel: ProgramEditorViewModel(injector: Injector(), initialTitle: "New program", initialInput: "input"))
    }
}
