package controller;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ProgramState.ProgramState;
import Model.Value.RefValue;
import Model.Value.Value;
import Repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {
    IRepository repository;
    ExecutorService executorService;


    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public IRepository getRepository() { return repository; }

    public void conservativeGarbageCollector(List<ProgramState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddressFromSymTable(p.getSymbol_table().allValues()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());

        programStates.forEach(p -> {
            p.getHeap().setHeapContent((HashMap<Integer, Value>) safeGarbageCollector
                    (symTableAddresses, getAddressFromHeap(p.getHeap().getHeapContent().values()),
                            p.getHeap().getHeapContent()));
        });
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> ( symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void oneStepForAllPrograms(List<ProgramState> programStates) throws InterruptedException, EvalException, ADTException, ExecException, IOException
    {
        //before the execution, print the ProgramState List into the log file
        programStates.forEach(programState ->
        {
            try
            {
                repository.logPrgStateExec(programState);
            }
            catch (IOException | ADTException e)
            {
                System.out.println(e.getMessage());
            }
        });

        //RUN concurrently one step for each of the existing ProgramStates
        //prepare the list of callables
        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        //start the execution of the callables
        //it returns the list of new created PrgStates (namely threads)
        List<ProgramState> newProgramList = executorService.invokeAll(callList).stream()
                .map(future -> {
                    try
                    {
                        return future.get();
                    }
                    catch (ExecutionException | InterruptedException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //add the new created threads to the list of existing threads
        programStates.addAll(newProgramList);

        //after the execution, print the PrgState List into the log file
        programStates.forEach(programState -> {
            try
            {
                repository.logPrgStateExec(programState);
            }
            catch (IOException | ADTException e)
            {
                System.out.println(e.getMessage());
            }
        });

        //save the current programs in the repository
        repository.setProgramStates(programStates);
    }

    public void oneStep() throws InterruptedException, ExecException, EvalException, ADTException, IOException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        oneStepForAllPrograms(programStates);
        conservativeGarbageCollector(programStates);
        //programStates = removeCompletedPrg(repository.getProgramList());
        executorService.shutdownNow();
        //repository.setProgramStates(programStates);
    }

    public void allStep() throws InterruptedException, EvalException, ADTException, ExecException, IOException {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrg(repository.getProgramList());
        while (programStates.size() > 0) {
            conservativeGarbageCollector(programStates);
            oneStepForAllPrograms(programStates);
            programStates = removeCompletedPrg(repository.getProgramList());
        }
        executorService.shutdownNow();
        repository.setProgramStates(programStates);
    }


    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream().filter(p -> !p.isNotCompleted()).collect(Collectors.toList());
    }



    public List<Integer> getAddressFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    public List<Integer> getAddressFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

}