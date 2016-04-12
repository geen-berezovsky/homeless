package ru.homeless.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

import ru.homeless.converters.ContractPointsTypeConverter;
import ru.homeless.converters.ContractResultTypeConverter;
import ru.homeless.converters.ShelterResultConverter;
import ru.homeless.entities.ContractControl;
import ru.homeless.entities.ContractPoints;
import ru.homeless.entities.ContractResult;
import ru.homeless.entities.ContractResult.PredefinedValues;
import ru.homeless.entities.Document;
import ru.homeless.entities.ServContract;
import ru.homeless.entities.ShelterResult;
import ru.homeless.entities.Worker;
import ru.homeless.primefaces.model.ContractPointsDataModel;
import ru.homeless.services.ClientService;
import ru.homeless.services.GenericService;
import ru.homeless.services.WorkerService;
import ru.homeless.util.Util;

@ManagedBean(name = "clientcontracts")
@ViewScoped
public class ClientContractsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public static Logger log = Logger.getLogger(ClientContractsBean.class);
    private List<ServContract> contractsList = null;
    private ServContract selectedContract;
    private Document selectedDocument;
    private Worker worker;
    private Document workerDocument;
    private List<ContractResult> contractResultTypes;


    private List<ShelterResult> shelterResults;
    private List<ContractControl> contractItems;
    private List<ContractPoints> contractPointsItems;
    private ContractPointsDataModel contractPointsDataModel;
    private List<ContractPoints> selectedNewContractPoints;
    private ContractControl selectedContractControl;
    private TimeZone timeZone;
    private String workerSelfData;
    private String workerPassportData;
    private String warrantData;

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    private StreamedContent file;

    @ManagedProperty(value = "#{ClientService}")
    private ClientService clientService;

    @ManagedProperty(value = "#{WorkerService}")
    private WorkerService workerService;

    public ClientContractsBean() {
        timeZone = TimeZone.getDefault();
    }

    public void downloadContract(int id) throws IOException {

        selectedContract = getClientService().getInstanceById(ServContract.class, id);

        String requestSuffix = "/getGeneratedContract?requestType=100&clientId="+ Util.getCurrentClient().getId() + "&contractId=" + selectedContract.getId() + "&workerId=" + selectedContract.getWorker().getId();
        String saveFilePath = "/tmp" + File.separator + "ClientContract.docx";
        String docType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String docName = "ClientContract.docx";

        try {
            file = Util.downloadDocument(requestSuffix, saveFilePath, docType, docName);
        } catch (Exception e) {
            log.error("Cannot download document from " +requestSuffix);
        }

    }

    public String formatDate(Date q) {
        if (q != null && !q.equals("")) {
            return Util.formatDate(q);
        } else {
            return "";
        }
    }

    public void reload() {
        HttpSession session = Util.getSession();
        worker = (Worker) session.getAttribute("worker");

        setWorkerSelfData(worker.getRules().getCaption() + " " + worker.getSurname() + " " + worker.getFirstname() + " " + worker.getMiddlename());
        workerDocument = getWorkerDocument();
        if (workerDocument != null && workerDocument.getDocPrefix() != null && workerDocument.getDocNum() != null && workerDocument.getDate() != null && workerDocument.getWhereAndWhom() != null) {
            setWorkerPassportData("Паспорт " + workerDocument.getDocPrefix() + " " + workerDocument.getDocNum() + " выдан " + formatDate(workerDocument.getDate()) + " " + workerDocument.getWhereAndWhom());
        } else {
            setWorkerPassportData("НЕДОСТАТОЧНО ДАННЫХ");
        }
        if (!worker.getWarrantNum().trim().equals("") && worker.getWarrantDate() != null) {
            setWarrantData("№" + worker.getWarrantNum() + " от " + formatDate(worker.getWarrantDate()));
        } else {
            setWarrantData("НЕДОСТАТОЧНО ДАННЫХ");
        }

        //update service plan items for actual contract
        if (selectedContract != null && selectedContract.getContractcontrols() != null) {
            contractItems = new ArrayList<ContractControl>(selectedContract.getContractcontrols());
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ClientFormBean cdb = context.getApplication().evaluateExpressionGet(context, "#{clientform}", ClientFormBean.class);
        cdb.updateContractsTabHeader();

    }

    public void deleteContract() {
        getClientService().deleteInstance(getClientService().getInstanceById(ServContract.class, selectedContract.getId()));
        reload();
    }

    public void editContract() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.update("edit_contract"); // next form should be updated immediatelly and manually!
        reload();
    }

    public void showAddContractDialog() {
        resetSelectedContract();
        //New docNum = count of all records from ServContract table plus 1
        selectedContract.setDocNum(String.valueOf(getClientService().getCountOfServContracts() + 1));
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("selectDocumentWv.show()");
    }

    public List<ServContract> getContractsList() {
        if (Util.getCurrentClient() == null) {
            return new ArrayList<>();
        } else {
            return getClientService().getInstancesByClientId(ServContract.class, Util.getCurrentClient().getId());
        }
    }

    public void setContractsList(List<ServContract> contractsList) {
        this.contractsList = contractsList;
    }

    public ServContract getSelectedContract() {
        return selectedContract;
    }

    public void setSelectedContract(ServContract selectedContract) {
        this.selectedContract = selectedContract;
    }

    public Document getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public void onRowSelect(SelectEvent event) {
        selectedDocument = (Document) event.getObject();
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public ContractControl getSelectedContractControl() {
        return selectedContractControl;
    }

    public void setSelectedContractControl(ContractControl selectedContractControl) {
        this.selectedContractControl = selectedContractControl;
    }

    public void updateSelectedContract() {
/*
    @vletovalio, please fix NPE. For testing just create new contract and insert new service plan items
        if ( isContractCompleted() ){
            //update from G.Sverdlin: "If the contract is successfully finalized, set the endDate for all subitems"
            if ( isContractSuccessefullyCompleted() ) {
                Set<ContractControl> set = selectedContract.getContractcontrols();
                for (ContractControl cc : set) {
                    cc.setEndDate(selectedContract.getStopDate());
                }
            }
        };
*/
        //OLD CODE STARTED
        //update from G.Sverdlin: "If the contract is successfully finalized, set the endDate for all subitems where it is null"
        if (selectedContract.getResult().getId() == 2) {
            Set<ContractControl> set = selectedContract.getContractcontrols();
            for (ContractControl cc : set) {
                if (cc.getEndDate() == null) {
                    cc.setEndDate(selectedContract.getStopDate());
                }
            }
        }
        //OLD CODE ENDED
        // ----------------------------------------------------------------------------------------------------
        getClientService().updateInstance(selectedContract);
        reload();
    }
    
    private boolean isContractCompleted(){
        return ! PredefinedValues.IN_PROGRESS.isSame(selectedContract.getResult());
    }
    
    private boolean isContractSuccessefullyCompleted(){
        return PredefinedValues.SUCCESSEFULLY_COMPLETED.isSame(selectedContract.getResult());
    }
    
    

    public List<ContractResult> getContractResultTypes() {
        List<ContractResult> list = getClientService().getInstances(ContractResult.class);
        ContractResultTypeConverter.contractResultTypesDB = new ArrayList<ContractResult>();
        ContractResultTypeConverter.contractResultTypesDB.addAll(list);
        return list;
    }

    public void setContractResultTypes(List<ContractResult> contractResultTypes) {
        this.contractResultTypes = contractResultTypes;
    }

    @PostConstruct
    // special for converter and selection data model!
    public void init() {
        contractResultTypes = new ArrayList<ContractResult>();
        contractResultTypes.addAll(getClientService().getInstances(ContractResult.class));
        ContractResultTypeConverter.contractResultTypesDB = new ArrayList<ContractResult>();
        ContractResultTypeConverter.contractResultTypesDB.addAll(contractResultTypes);

        contractPointsItems = new ArrayList<ContractPoints>();
        contractPointsItems.addAll(getClientService().getInstances(ContractPoints.class));
        ContractPointsTypeConverter.contractPointsTypesDB = new ArrayList<ContractPoints>();
        ContractPointsTypeConverter.contractPointsTypesDB.addAll(contractPointsItems);

        shelterResults = new ArrayList<ShelterResult>();
        shelterResults.addAll(getClientService().getInstances(ShelterResult.class));
        ShelterResultConverter.shelterResultList = new ArrayList<ShelterResult>();
        ShelterResultConverter.shelterResultList.addAll(shelterResults);


        contractPointsDataModel = new ContractPointsDataModel(contractPointsItems);

    }

    public void editServicePlanItem() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("contractItemsListWv.unselectAllRows()");
        rc.update("add_seriveplanitem"); // next form should be updated
        // immediatelly and manually!
        reload();
    }

    public void resetTableSelection() {
        selectedContractControl = new ContractControl();
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("contractItemsListWv.unselectAllRows()");
        rc.execute("addServiceItemWv.show()");
    }

    public List<ContractPoints> getContractPointsItems() {
        List<ContractPoints> list = getClientService().getInstances(ContractPoints.class);
        ContractPointsTypeConverter.contractPointsTypesDB = new ArrayList<ContractPoints>();
        ContractPointsTypeConverter.contractPointsTypesDB.addAll(list);
        return list;
    }

    public void setContractPointsItems(List<ContractPoints> contractPointsItems) {
        this.contractPointsItems = contractPointsItems;
    }

    public void initContractControl() {
        if (selectedContractControl == null) {
            selectedContractControl = new ContractControl();
        }
    }

    public void addServicePlanItem() {

        //According current table model (otherwise next code should be refactored)
        if (selectedContractControl.getServcontract() == null || selectedContractControl.getServcontract() == 0) {
            //This is new record
            selectedContractControl.setServcontract(selectedContract.getId());
            //getGenericService().addInstance(selectedContractControl);
            selectedContract.getContractcontrols().add(selectedContractControl);
        } else {
            getClientService().updateInstance(selectedContractControl);
        }

        getClientService().updateInstance(selectedContract);
        reload();
    }

    public void deleteServicePlanItem() {
        selectedContract.getContractcontrols().remove(selectedContractControl);
        getClientService().updateInstance(selectedContract);
        reload();
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    public void setWorkerService(WorkerService workerService) {
        this.workerService = workerService;
    }

    public Document getWorkerDocument() {
        return getWorkerService().getWorkerDocumentById(worker.getId());
    }

    public void setWorkerDocument(Document workerDocument) {
        this.workerDocument = workerDocument;
    }

    public void resetSelectedContract() {
        selectedContract = new ServContract();
    }

    public ContractPointsDataModel getContractPointsDataModel() {
        return contractPointsDataModel;
    }

    public void setContractPointsDataModel(ContractPointsDataModel contractPointsDataModel) {
        this.contractPointsDataModel = contractPointsDataModel;
    }

    public void validateStartDateFormat(FacesContext ctx, UIComponent component, Object value) {
        if (value == null || value.toString().trim().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Пожалуйста введите дату начала действия контракта", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result != null) {
            selectedContract.setStartDate(result);
        }
    }

    public void validateStopDateFormat(FacesContext ctx, UIComponent component, Object value) {
        if (value == null || value.toString().trim().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Пожалуйста введите дату окончания действия контракта", "Формат даты: ДД.ММ.ГГГГ");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ValidatorException(msg);
        }
        Date result = Util.validateDateFormat(ctx, component, value);
        if (result != null) {
            selectedContract.setStopDate(result);
        }
    }

    public void validateWorkerPasportDataOnly(FacesContext ctx, UIComponent component, Object value) {
        String str = value.toString();
        if (str.length() < 20) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Некорректны паспортные данные работника", "Пожалуйста, сначала добавьте собственные паспортные данные через Настройки->Мои Настройки!");
            throw new ValidatorException(msg);
        }
    }

    public void validateWorkerWarrantDataOnly(FacesContext ctx, UIComponent component, Object value) {
        String str = value.toString();
        if (str.contains("ЗАПОЛНИТЬ") || str.contains("НЕДОСТАТОЧНО")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Некорректный номер доверенности работника", "Пожалуйста, сначала добавьте информацию о доверенности и ее дате в свой профиль через Настройки->Мои Настройки!");
            throw new ValidatorException(msg);
        }
    }

    public void addNewContract() {

        if (selectedNewContractPoints.size() == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Не выбран ни один пункт сервисного плана", "Пожалуйста, выберите хотя бы один пункт сервисного плана!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {



            //for each contract point we need add different ContractControl objects related to this contract
            selectedContract.setContractcontrols(new HashSet<ContractControl>());
            for (ContractPoints c : getSelectedNewContractPoints()) {
                selectedContract.getContractcontrols().add(new ContractControl(selectedContract.getId(), c));
            }
            //then add new ServContract
            selectedContract.setClient(Util.getCurrentClient().getId());
            selectedContract.setCommentResult("");
            if (selectedDocument != null) {
                selectedContract.setDocumentId(selectedDocument.getId());
            } else {
                selectedContract.setDocumentId(0);
            }
            selectedContract.setResult(getClientService().getInstanceById(ContractResult.class, 1));
            selectedContract.setWorker(worker);

            getClientService().addInstance(selectedContract);

            //reset fields
            selectedContract = new ServContract();
            selectedNewContractPoints.clear();

            RequestContext rc = RequestContext.getCurrentInstance();
            rc.update("conlistId");
            rc.update(":select_document");
            rc.execute("addContractWv.hide()");
            reload();

        }
    }


    public void handleCloseEditContractDlg() {
        //auto saving
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("saveEditContractForm()");
        log.info("Dialog Edit Contract has been closed unexpectedly. The selected Contract is saved automatically.");
    }

    public void handleCloseEditServicePlanDlg() {
        //auto saving
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("saveEditServicePlanItemForm()");
        log.info("Dialog Edit Service Plan Item has been closed unexpectedly. The selected Service Plan Item is saved automatically.");
    }

    public String getWorkerSelfData() {
        return workerSelfData;
    }

    public void setWorkerSelfData(String workerSelfData) {
        this.workerSelfData = workerSelfData;
    }

    public String getWorkerPassportData() {
        return workerPassportData;
    }

    public void setWorkerPassportData(String workerPassportData) {
        this.workerPassportData = workerPassportData;
    }

    public String getWarrantData() {
        return warrantData;
    }

    public void setWarrantData(String warrantData) {
        this.warrantData = warrantData;
    }

    public List<ContractPoints> getSelectedNewContractPoints() {
        return selectedNewContractPoints;
    }

    public void setSelectedNewContractPoints(List<ContractPoints> selectedNewContractPoints) {
        this.selectedNewContractPoints = selectedNewContractPoints;
    }


    public List<ContractControl> getContractItems() {
        return contractItems;
    }

    public void setContractItems(List<ContractControl> contractItems) {
        this.contractItems = contractItems;
    }

    public List<ShelterResult> getShelterResults() {
        List<ShelterResult> list = getClientService().getInstances(ShelterResult.class);
        ShelterResultConverter.shelterResultList = new ArrayList<ShelterResult>();
        ShelterResultConverter.shelterResultList.addAll(list);
        return list;
    }

    public void setShelterResults(List<ShelterResult> shelterResults) {
        this.shelterResults = shelterResults;
    }


    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}

