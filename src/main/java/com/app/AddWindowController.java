package com.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/AddWindow.fxml")
public class AddWindowController {
    private TaskManager manager;

    @FXML
    private TextArea textArea;
    @FXML
    private CheckBox important;

    @Autowired
    public AddWindowController(TaskManager manager) {
        this.manager = manager;
    }

    public void addTask(ActionEvent actionEvent) {
        if(!textArea.getText().isEmpty()){
            Task task = new Task();
            task.setDone(false);
            task.setImportant(important.isSelected());
//            String text=
            task.setText(textArea.getText());
            textArea.getScene().getWindow().hide();
            manager.save(task);
        }
    }
}
