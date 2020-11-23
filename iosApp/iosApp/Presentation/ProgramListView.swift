//
//  ProgramListView.swift
//  iosApp
//
//  Created by Satoshi Kobayashi on 2020/11/23.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProgramListView: View {
    @ObservedObject
    var viewModel: ProgramListViewModel
    
    var body: some View {
        NavigationView {
            List(viewModel.programList, id: \.id) { program in
                programListRow(program)
            }
            .listStyle(InsetGroupedListStyle())
            .background(newProgramEditorLink())
            .navigationBarTitle("KMMBrainf*ck", displayMode: .large)
            .navigationBarItems(trailing: HStack {
                Button(action: {
                    viewModel.showNewEditor()
                }, label: {
                    Text("New")
                })
            })
        }
    }
    
    private func programListRow(_ program: Program) -> some View {
        let vm = viewModel.createOrReuseProgramEditorViewModelEdit(program: program)
        let programEditorView = ProgramEditorView(
            viewModel: vm,
            onDismiss:{
                viewModel.clearEditingProgramEditorViewModel()
            })
        return NavigationLink(
            destination: NavigationLazyDestination(programEditorView),
            label: {
                Text(program.title)
            })
    }
    
    private func newProgramEditorLink() -> some View {
        let vm = viewModel.createOrReuseProgramEditorViewModelNew()
        let programEditorView = ProgramEditorView(
            viewModel: vm,
            onDismiss: {
                viewModel.clearEditingProgramEditorViewModel()
            })
        return NavigationLink(
            destination: NavigationLazyDestination(programEditorView),
            isActive: $viewModel.isNewEditorShowing,
            label: {
                EmptyView()
            })
    }
}

struct ProgramListView_Previews: PreviewProvider {
    static let programList: [Program] = [
        Program(id: "id1", title: "title1", input: "input1"),
        Program(id: "id2", title: "title2", input: "input2"),
        Program(id: "id3", title: "title3", input: "input3")
    ]
    
    static var previews: some View {
        ProgramListView(viewModel: ProgramListViewModel(injector: Injector(), initialProgramList: programList))
    }
}
