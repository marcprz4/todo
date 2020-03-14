package com.app;

import javafx.beans.property.SimpleBooleanProperty;
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
    private TableColumn textCol;
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
        doneCol.setCellValueFactory(
                param -> new SimpleBooleanProperty(param.getValue().getDone())
        );

        doneCol.setCellFactory(CheckBoxTableCell.forTableColumn(doneCol));

        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));

        prioCol.setCellValueFactory(
                param -> new SimpleBooleanProperty(param.getValue().getDone())
        );

        prioCol.setCellFactory(CheckBoxTableCell.forTableColumn(doneCol));
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
        manager.updateById(task.getDone(),task.getImportant(),task.getText(),task.getId());
    }

    public void updateTable(MouseEvent mouseEvent) {
        Task task=table.getSelectionModel().getSelectedItem();
        update(task);
    }
}