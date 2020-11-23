//
//  ProgramEditorViewModel.swift
//  iosApp
//
//  Created by Satoshi Kobayashi on 2020/11/23.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class ProgramEditorViewModel: ObservableObject {
    @Published
    var title: String
    
    @Published
    var input: String
    
    @Published
    var output: ProgramOutput = .success(output: "")
    
    private(set) var program: Program?
    
    private let programService: ProgramService
    
    init(injector: Injector, initialTitle: String, initialInput: String) {
        title = initialTitle
        input = initialInput
        programService = injector.programService()
    }
    
    convenience init(injector: Injector, program: Program) {
        self.init(injector: injector, initialTitle: program.title, initialInput: program.input)
        self.program = program
    }
    
    func runProgram() {
        let result = Interpreter.Companion().execute(input: input)
        if let result = result as? Output.Success {
            output = .success(output: result.outputString)
        } else if let error = result as? Output.Error {
            output = .error(message: error.cause.message ?? "")
        }
    }
    
    func saveProgram() {
        if let program = program {
            programService.updateProgram(program: program, newInput: input) { result, error in
                self.program = result
            }
        } else {
            programService.saveProgram(title: title, input: input) { result, error in
                self.program = result
            }
        }
    }
}

enum ProgramOutput {
    case success(output: String)
    case error(message: String)
}
