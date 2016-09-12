package ability.reportusuario.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.bridges.jsf.common.FacesMessageUtil;

@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log _log = LogFactoryUtil.getLog(UsuarioBean.class);
	
	public void buscarUsuarios() throws SystemException, PortalException, IOException {
		String cadenaOrganizaciones = "";
		Workbook libro = new HSSFWorkbook();

//		Añadir una hoja al libro:
		Sheet hoja = libro.createSheet("Usuarios");

		int i=0;
		Row fila = hoja.createRow(i);
		
		Cell titleuserID = fila.createCell(0);
		titleuserID.setCellValue("userID");		
        Cell titleemailAddress = fila.createCell(1);
        titleemailAddress.setCellValue("emailAddress");
        Cell titlescreemName = fila.createCell(2);
        titlescreemName.setCellValue("screemName");
        Cell titlefirstName = fila.createCell(3);
        titlefirstName.setCellValue("firstName");
        Cell titlelastName= fila.createCell(4);
        titlelastName.setCellValue("lastName");
        Cell titleoganization = fila.createCell(5);
        titleoganization.setCellValue("Organization");
        i++;
        
        List<User> user_list = UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        for(User user: user_list){
        	if(user.getStatus() == 0){
	        	cadenaOrganizaciones = "";
	        	fila = hoja.createRow(i);
				Cell filauserID = fila.createCell(0);
	            filauserID.setCellValue(user.getUserId());   
	            Cell filaemailAddress = fila.createCell(1);
	            filaemailAddress.setCellValue(user.getEmailAddress());
	            Cell filascreemName = fila.createCell(2);
	            filascreemName.setCellValue(user.getScreenName());
	            Cell filafirstName = fila.createCell(3);
	            filafirstName.setCellValue(user.getFirstName());
	            Cell filalastName = fila.createCell(4);
	            filalastName.setCellValue(user.getLastName());
	            Cell filaorganization = fila.createCell(5);
				for(Organization organizacion: user.getOrganizations()){
					cadenaOrganizaciones = cadenaOrganizaciones+""+organizacion.getName()+", \n";
				}
				filaorganization.setCellValue(cadenaOrganizaciones);
				i++;
        	}
		}
		
		File archivoXLS = new File(Constants.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE+""+ System.currentTimeMillis()+"ReporteUsuarios.xls");
		OutputStream output = new FileOutputStream(archivoXLS);
		libro.write(output);
		output.close();
		
		FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
		_log.info(Constants.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +""+ archivoXLS.getName());
		
	}

}
