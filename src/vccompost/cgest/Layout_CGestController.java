package vccompost.cgest;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Layout_CGestController implements Initializable {
	
	CGest cGest;
	
	@FXML TextArea DataLog;
	@FXML Button BombaOn;
	@FXML Button BombaOff;
	@FXML Button ConsultaBomba;
	@FXML Button UpdBioRIP;
	@FXML Button UpdTabIP;
//	@FXML Button IFMuestreo;
	@FXML Button UpdGruPos;
	@FXML Button ActTablet;
	@FXML TextField BioRIP;
	@FXML TextField TabletIP;
	@FXML TextField GruPosDeseada;
	@FXML TextField GruPosActual;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cGest = new CGest(DataLog);
	}
	
	@FXML public void TabIPClick() {
		String TIP = TabletIP.getText();
		if(!TIP.equals(""))
			cGest.updTIP(TIP);
	}
	
	@FXML public void BioRIPClick() {
		String BRIP = BioRIP.getText();
		if(!BRIP.equals(""))
			cGest.updBIP(BRIP);
	}
	
	@FXML public void BombaOnClick() {
		cGest.mBomba(CGest.ENCENDER);
	}
	
	@FXML public void BombaOffClick() {
		cGest.mBomba(CGest.APAGAR);
	}
	
	@FXML public void ConsultaBombaClick() {
		cGest.mBomba(CGest.CONSULTAR);
	}
	
//	@FXML public void IFMuestreoClick() {
//		cGest.IFMU(BioRIP.getText());
//	}
	
	@FXML public void ActTabletClick() {
		cGest.updTablet();
	}
	
	@FXML public void UpdGruPosClick() {
		cGest.updGPos(Integer.parseInt(GruPosDeseada.getText()), Integer.parseInt(GruPosActual.getText()));
	}
}
