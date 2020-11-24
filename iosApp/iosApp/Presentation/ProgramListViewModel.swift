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
    
    private let injector: Injector
    
    private let programService: ProgramService
    
    private var editingProgramEditorViewModel: ProgramEditorViewModel?
    
    private var closeables: [Closeable] = []
    
    init(injector: Injector, initialProgramList: [Program]) {
        self.injector = injector
        programList = initialProgramList
        programService = injector.programService()
        loadProgramList()
    }
    
    deinit {
        for closeable in closeables {
            closeable.close()
        }
    }
    
    func createOrReuseProgramEditorViewModelEdit(program: Program) -> ProgramEditorViewModel {
        if let editingViewModel = editingProgramEditorViewModel,
           let editingProgram = editingViewModel.program,
           editingProgram.id == program.id {
            return editingViewModel
        }
        
        let editorViewModel = ProgramEditorViewModel(injector: injector, program: program)
        editingProgramEditorViewModel = editorViewModel
        return editorViewModel
    }
    
    func createOrReuseProgramEditorViewModelNew() -> ProgramEditorViewModel {
        if let editingViewModel = editingProgramEditorViewModel {
            return editingViewModel
        }
        
        let editorViewModel = ProgramEditorViewModel(
            injector: injector,
            initialTitle: "New Program",
            initialInput: ""
        )
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
        let closeable = programService.getPrograms().watch { [weak self] result in
            if let programList = result as? [Program] {
                self?.programList = programList
            }
        }
        closeables.append(closeable)
    }
}
