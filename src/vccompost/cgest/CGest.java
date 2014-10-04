package vccompost.cgest;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import vccompost.ide.Rutina_COM;
import vccompost.ide.Rutina_COM.TabletCOM;
import vccompost.tablet.Actualizar_Tablet;
import vccompost.vagoncontrol.VagonControl;
import vccompost.vagoncontrol.VagonControl.TListener;

public class CGest {
	
	public static final int ENCENDER = 1;
	public static final int APAGAR = 0;
	public static final int CONSULTAR = 2;
	
	VagonControl vControl;
	Actualizar_Tablet aTablet;
	Rutina_COM rutina;
	
	Thread thread;
	
	TextArea DataLog;
	
	boolean TMI = false;
	int nVag = 31;
	
	RoutineListener routineListener;
	public interface RoutineListener {
		public void OnRoutineReceived(int reactor, String pathA, String pathB);
	}
	public void setRoutineListener(RoutineListener listener) {
		routineListener = listener;
	}
	
	public CGest(TextArea log) {
		DataLog = log;
		initFunc();
	}
	
	public void initFunc() {
		initMU("10.0.0.55");
		aTablet = new Actualizar_Tablet("10.0.0.7", 2000);
		rutina = new Rutina_COM();
		rutina.setTabletCOM(new TabletCOM() {
			
			@Override
			public void DataProcesedB(int reactor, int sector, String pathB) {
				
			}
			
			@Override
			public void DataProcesedA(int reactor, int sector, String pathA) {
				
			}
			
			@Override
			public void DataProcesed(int reactor, String pathA, String pathB) {
				DataLog.appendText("Rutinas Recibidas para Reactor " + reactor + ":\r\n");
				DataLog.appendText("RA "+ pathA + "r\nRB " + pathB + "\r\n");
				if(routineListener != null)
					routineListener.OnRoutineReceived(reactor, pathA, pathB);
			}
		});
	}
	
	public void updTIP(String nTIP) {
		aTablet.Actualizar_IP(nTIP, 2000);
	}
	
	public void updBIP(String nBIP) {
		vControl.setIP(nBIP);
	}
	
	public void mBomba(int q) {
		if(q == ENCENDER) {
			vControl.BombaOn();
		}else if(q == APAGAR) {
			vControl.BombaOff();
		}else if(q == CONSULTAR) {
			vControl.consultaBomba();
		}
	}
	
	void initMU(String BioIP) {
		vControl = new VagonControl(BioIP);
		vControl.setTListener(new TListener() {
			
			@Override
			public void OnDataReceived(String linea) {
				String vData = genTabletD(vControl, nVag);
				aTablet.updRAMdata(nVag, vControl.hora, vData);
			}

			@Override
			public void OnInfoReceived(String linea) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						DataLog.appendText(linea);
					}
				});
			}
		});
		thread = new Thread(vControl);
		thread.setDaemon(true);
		thread.start();
		DataLog.appendText("Adquisicion Iniciada!\r\n");
		TMI = true;
	}
	
	String genTabletD(VagonControl vcont, int nvag) {
		int[] senT = new int[9];
		for(int i = 1; i < 9; i++)
			senT[i] = Math.round(vcont.sen[i]);
		String datG = "1#" + (nvag + 1) + "#8#"+
		"20"+";"+"500"+";" + vcont.hora+";"+senT[1]+";"+senT[1]+";"+senT[1]+";"+senT[1]+"&"+
		"60"+";"+"500"+";" + vcont.hora+";"+senT[2]+";"+senT[2]+";"+senT[2]+";"+senT[2]+"&"+
		"940"+";"+"500"+";"+ vcont.hora+";"+senT[3]+";"+senT[3]+";"+senT[3]+";"+senT[3]+"&"+
		"980"+";"+"500"+";"+ vcont.hora+";"+senT[4]+";"+senT[4]+";"+senT[4]+";"+senT[4]+"&"+
		"980"+";"+"970"+";"+ vcont.hora+";"+senT[5]+";"+senT[5]+";"+senT[5]+";"+senT[5]+"&"+
		"500"+";"+"500"+";"+ vcont.hora+";"+senT[6]+";"+senT[6]+";"+senT[6]+";"+senT[6]+"&"+
		"500"+";"+"900"+";"+ vcont.hora+";"+senT[7]+";"+senT[7]+";"+senT[7]+";"+senT[7]+"&"+
		"500"+";"+"970"+";"+ vcont.hora+";"+senT[8]+";"+senT[8]+";"+senT[8]+";"+senT[8]+"&/";
		return datG;
	}
	
	public void updTablet() {
		aTablet.consultar();
	}
	
	public void updGPos(int Rdeseado, int Ractual) {
		aTablet.enviarPGru(Rdeseado, Ractual);
	}
}