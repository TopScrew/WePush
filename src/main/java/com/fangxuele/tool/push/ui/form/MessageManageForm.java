package com.fangxuele.tool.push.ui.form;

import com.fangxuele.tool.push.App;
import com.fangxuele.tool.push.dao.*;
import com.fangxuele.tool.push.domain.*;
import com.fangxuele.tool.push.logic.MessageTypeEnum;
import com.fangxuele.tool.push.ui.form.msg.MsgFormFactory;
import com.fangxuele.tool.push.util.JTableUtil;
import com.fangxuele.tool.push.util.MybatisUtil;
import com.google.common.collect.Maps;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * MessageManageForm
 * </pre>
 *
 * @author <a href="https://github.com/rememberber">RememBerBer</a>
 * @since 2019/5/6.
 */
@Getter
public class MessageManageForm {

    private JPanel messageManagePanel;
    private JTable msgHistable;
    private JButton msgHisTableDeleteButton;
    private JButton createMsgButton;
    private JComboBox accountComboBox;
    private JPanel panel1;

    private static MessageManageForm messageManageForm;

    public static Map<String, Integer> accountMap;

    private static TAccountMapper accountMapper = MybatisUtil.getSqlSession().getMapper(TAccountMapper.class);

    private static TMsgKefuMapper msgKefuMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuMapper.class);
    private static TMsgKefuPriorityMapper msgKefuPriorityMapper = MybatisUtil.getSqlSession().getMapper(TMsgKefuPriorityMapper.class);
    private static TMsgWxUniformMapper wxUniformMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxUniformMapper.class);
    private static TMsgMaTemplateMapper msgMaTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaTemplateMapper.class);
    private static TMsgMaSubscribeMapper msgMaSubscribeMapper = MybatisUtil.getSqlSession().getMapper(TMsgMaSubscribeMapper.class);
    private static TMsgMpTemplateMapper msgMpTemplateMapper = MybatisUtil.getSqlSession().getMapper(TMsgMpTemplateMapper.class);
    private static TMsgMpSubscribeMapper msgMpSubscribeMapper = MybatisUtil.getSqlSession().getMapper(TMsgMpSubscribeMapper.class);
    private static TMsgSmsMapper msgSmsMapper = MybatisUtil.getSqlSession().getMapper(TMsgSmsMapper.class);
    private static TMsgMailMapper msgMailMapper = MybatisUtil.getSqlSession().getMapper(TMsgMailMapper.class);
    private static TMsgWxCpMapper msgWxCpMapper = MybatisUtil.getSqlSession().getMapper(TMsgWxCpMapper.class);
    private static TMsgHttpMapper msgHttpMapper = MybatisUtil.getSqlSession().getMapper(TMsgHttpMapper.class);
    private static TMsgDingMapper msgDingMapper = MybatisUtil.getSqlSession().getMapper(TMsgDingMapper.class);
    private static TWxAccountMapper wxAccountMapper = MybatisUtil.getSqlSession().getMapper(TWxAccountMapper.class);

    public static boolean accountSwitchComboBoxListenIgnore = false;

    private MessageManageForm() {
    }

    public static MessageManageForm getInstance() {
        if (messageManageForm == null) {
            messageManageForm = new MessageManageForm();
        }
        return messageManageForm;
    }

    /**
     * 初始化消息列表
     */
    public static void init() {
        messageManageForm = getInstance();

        initAccountComboBox();

        initMessageList();

        MessageEditForm.getInstance().getMsgNameField().setText("");
        MsgFormFactory.getMsgForm().clearAllField();
    }

    private static void initAccountComboBox() {
        accountMap = Maps.newHashMap();
        messageManageForm.getAccountComboBox().removeAllItems();
        int msgType = MessageTypeEnum.getMsgTypeForAccount();
        List<TAccount> tAccountList = accountMapper.selectByMsgType(msgType);
        for (TAccount tAccount : tAccountList) {
            String accountName = tAccount.getAccountName();
            Integer accountId = tAccount.getId();
            messageManageForm.getAccountComboBox().addItem(accountName);
            accountMap.put(accountName, accountId);
        }
    }

    public static void initMessageList() {
        // 历史消息管理
        String[] headerNames = {"消息名称", "ID"};
        DefaultTableModel model = new DefaultTableModel(null, headerNames);
        messageManageForm.getMsgHistable().setModel(model);
        // 隐藏表头
        JTableUtil.hideTableHeader(messageManageForm.getMsgHistable());

        int msgType = App.config.getMsgType();

        String selectedAccountName = (String) messageManageForm.getAccountComboBox().getSelectedItem();
        Integer selectedAccountId = accountMap.get(selectedAccountName);

        Object[] data;

        switch (msgType) {
            case MessageTypeEnum.KEFU_CODE:
                List<TMsgKefu> tMsgKefuList = msgKefuMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgKefu tMsgKefu : tMsgKefuList) {
                    data = new Object[2];
                    data[0] = tMsgKefu.getMsgName();
                    data[1] = tMsgKefu.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.KEFU_PRIORITY_CODE:
                List<TMsgKefuPriority> tMsgKefuPriorityList = msgKefuPriorityMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgKefuPriority tMsgKefuPriority : tMsgKefuPriorityList) {
                    data = new Object[2];
                    data[0] = tMsgKefuPriority.getMsgName();
                    data[1] = tMsgKefuPriority.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.WX_UNIFORM_MESSAGE_CODE:
                List<TMsgWxUniform> tMsgWxUniformList = wxUniformMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgWxUniform tMsgWxUniform : tMsgWxUniformList) {
                    data = new Object[2];
                    data[0] = tMsgWxUniform.getMsgName();
                    data[1] = tMsgWxUniform.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.MA_TEMPLATE_CODE:
                List<TMsgMaTemplate> tMsgMaTemplateList = msgMaTemplateMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgMaTemplate tMsgMaTemplate : tMsgMaTemplateList) {
                    data = new Object[2];
                    data[0] = tMsgMaTemplate.getMsgName();
                    data[1] = tMsgMaTemplate.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.MA_SUBSCRIBE_CODE:
                List<TMsgMaSubscribe> tMsgMaSubscribeList = msgMaSubscribeMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgMaSubscribe tMsgMaSubscribe : tMsgMaSubscribeList) {
                    data = new Object[2];
                    data[0] = tMsgMaSubscribe.getMsgName();
                    data[1] = tMsgMaSubscribe.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.MP_TEMPLATE_CODE:
                List<TMsgMpTemplate> tMsgMpTemplateList = msgMpTemplateMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgMpTemplate tMsgMpTemplate : tMsgMpTemplateList) {
                    data = new Object[2];
                    data[0] = tMsgMpTemplate.getMsgName();
                    data[1] = tMsgMpTemplate.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.MP_SUBSCRIBE_CODE:
                List<TMsgMpSubscribe> tMsgMpSubscribeList = msgMpSubscribeMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgMpSubscribe tMsgMpSubscribe : tMsgMpSubscribeList) {
                    data = new Object[2];
                    data[0] = tMsgMpSubscribe.getMsgName();
                    data[1] = tMsgMpSubscribe.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.EMAIL_CODE:
                List<TMsgMail> tMsgMailList = msgMailMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgMail tMsgMail : tMsgMailList) {
                    data = new Object[2];
                    data[0] = tMsgMail.getMsgName();
                    data[1] = tMsgMail.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.WX_CP_CODE:
                List<TMsgWxCp> tMsgWxCpList = msgWxCpMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgWxCp tMsgWxCp : tMsgWxCpList) {
                    data = new Object[2];
                    data[0] = tMsgWxCp.getMsgName();
                    data[1] = tMsgWxCp.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.HTTP_CODE:
                List<TMsgHttp> tMsgHttpList = msgHttpMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgHttp tMsgHttp : tMsgHttpList) {
                    data = new Object[2];
                    data[0] = tMsgHttp.getMsgName();
                    data[1] = tMsgHttp.getId();
                    model.addRow(data);
                }
                break;
            case MessageTypeEnum.DING_CODE:
                List<TMsgDing> tMsgDingList = msgDingMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgDing tMsgDing : tMsgDingList) {
                    data = new Object[2];
                    data[0] = tMsgDing.getMsgName();
                    data[1] = tMsgDing.getId();
                    model.addRow(data);
                }
                break;
            default:
                List<TMsgSms> tMsgSmsList = msgSmsMapper.selectByMsgTypeAndAccountId(msgType, selectedAccountId);
                for (TMsgSms tMsgSms : tMsgSmsList) {
                    data = new Object[2];
                    data[0] = tMsgSms.getMsgName();
                    data[1] = tMsgSms.getId();
                    model.addRow(data);
                }
                break;
        }

        // 隐藏id列
        JTableUtil.hideColumn(messageManageForm.getMsgHistable(), 1);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        messageManagePanel = new JPanel();
        messageManagePanel.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 0), -1, -1));
        messageManagePanel.setMaximumSize(new Dimension(-1, -1));
        messageManagePanel.setMinimumSize(new Dimension(-1, -1));
        messageManagePanel.setPreferredSize(new Dimension(280, -1));
        panel2.add(messageManagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        messageManagePanel.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        msgHistable = new JTable();
        msgHistable.setGridColor(new Color(-12236470));
        msgHistable.setRowHeight(36);
        msgHistable.setShowVerticalLines(false);
        scrollPane1.setViewportView(msgHistable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        messageManagePanel.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        msgHisTableDeleteButton = new JButton();
        msgHisTableDeleteButton.setIcon(new ImageIcon(getClass().getResource("/icon/remove.png")));
        msgHisTableDeleteButton.setText("删除");
        panel3.add(msgHisTableDeleteButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        createMsgButton = new JButton();
        createMsgButton.setEnabled(true);
        createMsgButton.setIcon(new ImageIcon(getClass().getResource("/icon/add.png")));
        createMsgButton.setText("新建");
        panel3.add(createMsgButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        messageManagePanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        accountComboBox = new JComboBox();
        panel1.add(accountComboBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}
