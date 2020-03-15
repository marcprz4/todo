package com.app;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/MainWindow.fxml")
public class MainWindowController implements Initializable {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private TaskManager manager;
    private ObservableList<Task> list;

    @FXML
    private TableView<Task> table;
    @FXML
    private TableColumn<Task, Boolean> doneCol;
    @FXML
    private TableColumn<Task, String> textCol;
    @FXML
    private TableColumn<Task, Boolean> prioCol;

    @Autowired
    public MainWindowController(TaskManager manager) {
        this.manager = manager;
    }

    public void addTask(ActionEvent actionEvent) throws IOException {
        FxWeaver fxWeaver = Main.springContext.getBean(FxWeaver.class);
        rootNode = fxWeaver.loadView(AddWindowController.class);
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
        refresh();
    }

    public void deleteTask(ActionEvent actionEvent) {
        if(!table.getSelectionModel().isEmpty())
        manager.deleteById(table.getSelectionModel().getSelectedItem().getId());
        refresh();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        doneCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Task, Boolean> param) {
                        Task task = param.getValue();

                        SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(task.getDone());

                        booleanProp.addListener(new ChangeListener<Boolean>() {

                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                    Boolean newValue) {
                                task.setDone(newValue);
                                update(task);
                            }
                        });
                        return booleanProp;
                    }
                });
        //
        doneCol.setCellFactory(new Callback<TableColumn<Task, Boolean>,
                TableCell<Task, Boolean>>() {
                    @Override
                    public TableCell<Task, Boolean> call(TableColumn<Task, Boolean> p) {
                        CheckBoxTableCell<Task, Boolean> cell = new CheckBoxTableCell<Task, Boolean>();
                        cell.setAlignment(Pos.CENTER);
                        return cell;
                    }
                });

        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));

        prioCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Task, Boolean> param) {
                Task task = param.getValue();

                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(task.getDone());

                booleanProp.addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {
                        task.setImportant(newValue);
                        update(task);
                    }
                });
                return booleanProp;
            }
        });
        //
        prioCol.setCellFactory(new Callback<TableColumn<Task, Boolean>,
                TableCell<Task, Boolean>>() {
            @Override
            public TableCell<Task, Boolean> call(TableColumn<Task, Boolean> p) {
                CheckBoxTableCell<Task, Boolean> cell = new CheckBoxTableCell<Task, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        refresh();
    }

    private void refresh() {
        table.getItems().clear();
        list = FXCollections.observableList(manager.findAll());
        if (list.size() > 0) {
            table.getItems().addAll(list);
        }
    }

    private void update(Task task) {
        manager.update(task);
    }
}