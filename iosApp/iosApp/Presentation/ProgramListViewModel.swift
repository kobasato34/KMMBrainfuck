//
//  ProgramListViewModel.swift
//  iosApp
//
//  Created by Satoshi Kobayashi on 2020/11/23.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class ProgramListViewModel: ObservableObject {
    @Published
    var programList: [Program]
    
    @Published
    var isNewEditorShowing: Bool = false
    
    private let programService: ProgramService
    
    private var editingProgramEditorViewModel: ProgramEditorViewModel?
    
    init(injector: Injector, initialProgramList: [Program]) {
        programList = initialProgramList
        programService = injector.programService()
        loadProgramList()
    }
    
    func createOrReuseProgramEditorViewModelEdit(program: Program) -> ProgramEditorViewModel {
        if let editingViewModel = editingProgramEditorViewModel,
           let editingProgram = editingViewModel.program,
           editingProgram.id == program.id {
            return editingViewModel
        }

        let editorViewModel = ProgramEditorViewModel(injector: Injector(), program: program)
        editingProgramEditorViewModel = editorViewModel
        return editorViewModel
    }

    func createOrReuseProgramEditorViewModelNew() -> ProgramEditorViewModel {
        if let editingViewModel = editingProgramEditorViewModel {
            return editingViewModel
        }

        let editorViewModel = ProgramEditorViewModel(injector: Injector(), initialTitle: "New Program", initialInput: "")
        editingProgramEditorViewModel = editorViewModel
        return editorViewModel
    }
    
    func clearEditingProgramEditorViewModel() {
        editingProgramEditorViewModel = nil
    }
    
    func showNewEditor() {
        isNewEditorShowing = true
    }
    
    private func loadProgramList() {
        programService.getPrograms { [weak self] list in
            self?.programList = list
        }
    }
}
